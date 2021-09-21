package com.zipper.api.module

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.api.module.bean.ApiModuleInfo
import com.zipper.api.module.bean.ApiModuleVersion
import com.zipper.core.utils.FileUtil
import com.zipper.core.utils.L
import com.zipper.core.utils.SpUtil
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.*
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
object ApiModuleManager {

    private const val TAG: String = "ApiModuleManager"

    private const val MODULE_SP_NAME = "sp_api_module"

    private const val ASSETS_MODULE_FILENAME = "api_module.json"

    private const val ASSETS_MODULE_VERSION_FILENAME = "api_module_version.json"

    private const val LOCAL_MODULE_FILENAME = "api_module.json"

    private const val LOCAL_MODULE_DIR = "api_modules"

    /* 应用上下文 */
    private lateinit var context: Context

    /* sp存储类 */
    private lateinit var spWrapper: SpUtil.SharedPreferencesWrapper

    /* 模块接口 */
    private val moduleMap: MutableMap<String, IApiModule> = mutableMapOf()

    /* 模块配置信息列表 */
    private val mModuleInfoList: MutableList<ApiModuleInfo> = mutableListOf()

    /* 模块上下文 */
    private val moduleContextMap: MutableMap<String, ApiModuleContext> = mutableMapOf()

    /* 模块运行状态 */
    private val moduleRunningStatusMap: MutableMap<String, Boolean> = mutableMapOf()

    val moduleInfoList: List<ApiModuleInfo> get() = mModuleInfoList

    private lateinit var mModuleInfoFile: File

    val moduleInfoFile: File get() = mModuleInfoFile

    private val mTaskRunningShareFlow:MutableSharedFlow<TaskRunningEvent<*>> = MutableSharedFlow(
        1,
        10,
        BufferOverflow.DROP_OLDEST
    )

    val taskRunningFlow: Flow<TaskRunningEvent<*>> get() = mTaskRunningShareFlow

    fun initModule(context: Context) {
        this.context = context.applicationContext
        spWrapper = SpUtil.instance(MODULE_SP_NAME)
        mModuleInfoFile = File(context.filesDir, LOCAL_MODULE_FILENAME)
        if (!mModuleInfoFile.exists()) {
            moduleInfoFile.createNewFile()
            // 首次加载读取Asset配置
            loadAssetsModuleConfig()
            // 写入配置到本地文件中
            saveModuleInfo()
            // 将Asset中的模块拷贝到本地
            copyAssetsModuleToLocal()
        } else {
            // 加载本地模块配置
            loadLocalModuleConfig()
        }
        // 模块更新检查
        checkModuleAssetUpdate()
        // 初始化模块代码
        initModuleCode()
        // 初始化时会剔除无法加载的模块
        saveModuleInfo(moduleInfoFile)
    }

    /**
     * 检查模块是否存在
     * @return true 存在 false 不存在
     *
     */
    fun checkModuleExists(key: String): Boolean {
        try {
            val moduleInfo = mModuleInfoList.find { it.moduleKey == key }

            if (moduleInfo == null) {
                L.d(TAG, "未找到 $key 对应的模块信息")
                return false
            }
            var module = moduleMap[key]
            if (module == null) {
                L.d(TAG, "未找到 $key 对应的模块 尝试重新加载模块")
                module = loadApiModuleClass(moduleInfo)
            }

            if (module == null) {
                L.d(TAG, "重新加载 $key 对应的模块后仍未找到模块")
                return false
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 调用模块入口
     */
    fun callModuleMain(key: String, variable: Map<String, String>) {
        if (!checkModuleExists(key)) {
            return
        }
        val module = moduleMap[key]!!
        try {
            moduleRunningStatusMap[key] = true
            // 注入变量
            module.setVariable(variable)
            // 执行模块主代码
            module.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            moduleRunningStatusMap[key] = false
        }
    }

    /**
     * 检查模块是否运行（仅调用CallModuleMain)
     */
    fun checkModuleIsRun(key: String): Boolean {
        if (!checkModuleExists(key)) {
            return false
        }
        return moduleRunningStatusMap[key] ?: false
    }

    /**
     * 停止模块
     */
    fun stopModule(moduleKey: String): Boolean{
        if (!checkModuleExists(moduleKey)) {
            return false
        }
        moduleMap[moduleKey]?.stop()
        return true
    }

    /**
     * 禁用模块
     */
    fun banModule(moduleKey: String) : Boolean{
        if (moduleKey.isEmpty()) {
            MLog.d(TAG, "【禁用模块】模块key不能为空")
            return false
        }

        var banModule: ApiModuleInfo? = null
        for (moduleInfo in mModuleInfoList) {
            if (moduleKey == moduleInfo.moduleKey) {
                banModule = moduleInfo
                break
            }
        }
        return banModule?.let {
            // 移除运行状态
            moduleRunningStatusMap.remove(it.moduleKey)
            // 设置模块为禁用状态
            it.isBan = true
            // TODO 移除任务调度
            // 移除模块
            moduleMap.remove(it.moduleKey)?.release()
            // 移除模块上下文
            moduleContextMap.remove(it.moduleKey)
            // 保存设置
            saveModuleInfo()
            true
        } ?: false
    }

    /**
     * 恢复模块
     */
    fun resumeModule(moduleKey: String): Boolean {
        if (moduleKey.isEmpty()) {
            MLog.d(TAG, "【恢复禁用模块】模块key不能为空")
            return false
        }
        var banModule: ApiModuleInfo? = null
        for (moduleInfo in mModuleInfoList) {
            if (moduleKey == moduleInfo.moduleKey) {
                banModule = moduleInfo
                break
            }
        }

        if (banModule == null){
            return false
        }

        return banModule.let {
            // 解除模块禁用状态
            it.isBan = false
            // 加载模块
            loadApiModuleClass(it)
            // 加载模块上下文资源
            loadModuleContext(it)
            // 保存设置
            saveModuleInfo(moduleInfoFile)
            // TODO 调度模块
            true
        }
    }

    fun removeModuleByKey(moduleKey: String): Boolean{
        if (banModule(moduleKey)){
            // 删除配置
            var banModule: ApiModuleInfo? = null
            for (moduleInfo in mModuleInfoList) {
                if (moduleKey == moduleInfo.moduleKey) {
                    banModule = moduleInfo
                    break
                }
            }
            if (banModule == null){
                return false
            }

            return banModule.let {
                mModuleInfoList.remove(it)
                try {
                    val file = File(it.modulePath)
                    if (file.exists()){
                        file.delete()
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                saveModuleInfo()
                true
            }
        }
        return false
    }

    fun getModuleByKey(moduleKey: String): IApiModule? {
        return moduleMap[moduleKey]
    }

    private fun loadAssetsModuleConfig() {
        context.assets.open(ASSETS_MODULE_FILENAME).use {
            mModuleInfoList.clear()
            moduleMap.clear()
            val moduleList: List<ApiModuleInfo> = Gson().fromJson(InputStreamReader(it),
                object : TypeToken<List<ApiModuleInfo>>() {}.type
            )
            mModuleInfoList.addAll(moduleList)
        }
    }

    private fun loadLocalModuleConfig() {
        FileReader(mModuleInfoFile).use {
            mModuleInfoList.clear()
            val moduleList: List<ApiModuleInfo> =
                Gson().fromJson(it, object : TypeToken<List<ApiModuleInfo>>() {}.type)
            mModuleInfoList.addAll(moduleList)
        }
    }

    /**
     * 检查模块更新
     *
     * 如果覆盖安装后，asset配置的模块版本和本地不同，替换本地的模块
     */
    private fun checkModuleAssetUpdate() {
        try {
            var hasUpdate = false
            context.assets.open(ASSETS_MODULE_VERSION_FILENAME).use {
                val updateModuleMap: Map<String, ApiModuleVersion> =
                    Gson().fromJson(
                        InputStreamReader(it),
                        object : TypeToken<Map<String, ApiModuleVersion>>() {}.type
                    )

                for (moduleInfo in mModuleInfoList) {
                    val moduleKey = moduleInfo.moduleKey
                    val moduleVersion = updateModuleMap[moduleKey]
                    if (moduleVersion != null) {
                        // debug
                        if (moduleVersion.moduleMd5 == "debug"
                            || (moduleVersion.moduleVersion > moduleInfo.moduleVersion
                                    && moduleVersion.moduleMd5 != moduleInfo.moduleMd5)
                        ) {
                            copyAssetModule(moduleVersion.modulePath, moduleInfo.modulePath)
                            moduleInfo.moduleVersion = moduleVersion.moduleVersion
                            moduleInfo.moduleMd5 = moduleVersion.moduleMd5
                            hasUpdate = true
                        }
                    }
                }
            }
            if (hasUpdate) {
                saveModuleInfo(moduleInfoFile)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveModuleInfo() {
        saveModuleInfo(moduleInfoFile)
    }

    private fun saveModuleInfo(moduleInfoFile: File) {
        try {
            FileWriter(moduleInfoFile).use {
                val moduleInfoJson = Gson().toJson(mModuleInfoList)
                it.write(moduleInfoJson)
                it.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyAssetModule(assetFilePath: String, localFilePath: String): Boolean {
        try {
            return context.assets.open(assetFilePath).use {
                val moduleFile = File(localFilePath)
                if (!moduleFile.exists()) {
                    moduleFile.createNewFile()
                }
                FileUtil.copyFile(it, FileOutputStream(moduleFile))
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun copyAssetsModuleToLocal() {
        val dirFile = File(context.filesDir, LOCAL_MODULE_DIR)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        for (moduleInfo in mModuleInfoList) {
            val localPath = if (moduleInfo.moduleFileName.isNullOrEmpty()) {
                File(dirFile, moduleInfo.moduleName).absolutePath
            } else {
                File(dirFile, moduleInfo.moduleFileName).absolutePath
            }
            if (copyAssetModule(moduleInfo.modulePath, localPath)) {
                moduleInfo.moduleStoreType = ApiModuleInfo.ModuleStoreType.TYPE_LOCAL
                moduleInfo.modulePath = localPath
            }
        }
        // 过滤错误的Module
        val newModuleInfo =
            mModuleInfoList.filter { it.moduleStoreType == ApiModuleInfo.ModuleStoreType.TYPE_LOCAL }.toList()

        mModuleInfoList.retainAll(newModuleInfo)

        // 写入新的配置到本地
        saveModuleInfo()
    }

    private fun initModuleCode() {
        val unExistsModule = mutableListOf<ApiModuleInfo>()

        for (moduleInfo in mModuleInfoList) {
            val moduleFile = File(moduleInfo.modulePath)
            if (!moduleFile.exists()) {
                unExistsModule.add(moduleInfo)
                continue
            }
            // 添加资源
            loadModuleContext(moduleInfo)
        }
        mModuleInfoList.removeAll(unExistsModule)
        // 加载插件代码
        addModuleCode(mModuleInfoList.map { it.modulePath }.toList())

        for (moduleInfo in mModuleInfoList) {
            loadApiModuleClass(moduleInfo)
        }
    }

    private fun addModuleCode(modulePath: List<String>) {
        // 加载插件代码
        ProxyClassLoader.hookElement(
            modulePath,
            context.classLoader
        )
    }

    private fun loadApiModuleClass(moduleInfo: ApiModuleInfo): IApiModule? {
        var resultModule: IApiModule? = null
        try {
            val moduleContext = moduleContextMap[moduleInfo.moduleKey]

            if (moduleContext == null) {
                L.d(TAG, "the ${moduleInfo.moduleName} moduleContext == null")
                return resultModule
            }
            val moduleImplClass = Class.forName(moduleInfo.moduleImplClass)
            val moduleConstructor =
                moduleImplClass.getDeclaredConstructor(ApiModuleContext::class.java)
            resultModule = moduleConstructor.newInstance(moduleContext) as IApiModule
            moduleMap[moduleInfo.moduleKey] = resultModule
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultModule
    }

    /**
     * 加载模块上下文
     */
    private fun loadModuleContext(moduleInfo: ApiModuleInfo) {
        try {
            val moduleContext = ApiModuleContext(context, File(moduleInfo.modulePath))
            moduleContextMap[moduleInfo.moduleKey] = moduleContext
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}