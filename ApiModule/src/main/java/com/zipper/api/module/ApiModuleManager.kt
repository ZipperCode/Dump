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
    private val _moduleInfoList: MutableList<ApiModuleInfo> = mutableListOf()

    /* 模块上下文 */
    private val moduleContextMap: MutableMap<String, ApiModuleContext> = mutableMapOf()

    /* 模块运行状态 */
    private val moduleRunningStatusMap: MutableMap<String, Boolean> = mutableMapOf()

    val moduleInfoList: List<ApiModuleInfo> get() = _moduleInfoList

    private lateinit var mModuleInfoFile: File

    val moduleInfoFile: File get() = mModuleInfoFile

    fun initModule(context: Context) {
        this.context = context.applicationContext
        spWrapper = SpUtil.instance(MODULE_SP_NAME)
        mModuleInfoFile = File(context.filesDir, LOCAL_MODULE_FILENAME)
        if (!mModuleInfoFile.exists()) {
            moduleInfoFile.createNewFile()
            // 首次加载读取Asset配置
            loadAssetsModuleConfig()
            // 写入配置到本地文件中
            saveModuleInfo(mModuleInfoFile)
            // 将Asset中的模块拷贝到本地
            copyAssetsModuleToLocal(mModuleInfoFile)
        }
        // 加载本地模块配置
        loadLocalModuleConfig(mModuleInfoFile)


        // 初始化模块代码
        initModuleCode()
        // 初始化时会剔除无法加载的模块
        saveModuleInfo(moduleInfoFile)
    }

    fun checkModule(key: String): Boolean {
        try {
            val moduleInfo = _moduleInfoList.find { it.moduleKey == key }

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

    fun callModuleMain(key: String, variable: Map<String, String>) {
        if (!checkModule(key)) {
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

    fun checkModuleIsRun(key: String): Boolean {
        if (!checkModule(key)) {
            return false
        }
        return moduleRunningStatusMap[key] ?: false
    }

    fun banModule(moduleKey: String) {
        // TODO
    }

    fun getModuleByKey(moduleKey: String): IApiModule? {
        return moduleMap[moduleKey]
    }

    private fun loadAssetsModuleConfig() {
        context.assets.open(ASSETS_MODULE_FILENAME).use {
            _moduleInfoList.clear()
            moduleMap.clear()
            val moduleList: List<ApiModuleInfo> = Gson().fromJson(InputStreamReader(it),
                object : TypeToken<List<ApiModuleInfo>>() {}.type)
            _moduleInfoList.addAll(moduleList)
        }
    }

    private fun loadLocalModuleConfig(moduleInfoFile: File) {
        FileReader(moduleInfoFile).use {
            _moduleInfoList.clear()
            val moduleList: List<ApiModuleInfo> =
                Gson().fromJson(it, object : TypeToken<List<ApiModuleInfo>>() {}.type)
            _moduleInfoList.addAll(moduleList)
        }
    }

    private fun checkModuleAssetUpdate() {
        try {
            context.assets.open(ASSETS_MODULE_VERSION_FILENAME).use {
                val updateModuleMap: Map<String, ApiModuleVersion> =
                    Gson().fromJson(InputStreamReader(it),
                        object : TypeToken<Map<String, ApiModuleVersion>>() {}.type
                    )

                for (moduleInfo in _moduleInfoList) {
                    val moduleKey = moduleInfo.moduleKey
                    val moduleVersion = updateModuleMap[moduleKey]
                    if (moduleVersion != null) {
                        // debug
                        if(moduleVersion.moduleMd5 == "debug"
                            || (moduleVersion.moduleVersion > moduleInfo.moduleVersion
                                    && moduleVersion.moduleMd5 != moduleInfo.moduleMd5)){
                            copyAssetModule(moduleVersion.modulePath,moduleInfo.modulePath)
                            moduleInfo.moduleVersion = moduleVersion.moduleVersion
                            moduleInfo.moduleMd5 = moduleVersion.moduleMd5
                        }
                    }
                }
            }

            saveModuleInfo(moduleInfoFile)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun saveModuleInfo(moduleInfoFile: File) {
        FileWriter(moduleInfoFile).use {
            val moduleInfoJson = Gson().toJson(_moduleInfoList)
            it.write(moduleInfoJson)
            it.flush()
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

    private fun copyAssetsModuleToLocal(moduleInfoFile: File) {
        val dirFile = File(context.filesDir, LOCAL_MODULE_DIR)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        for (moduleInfo in _moduleInfoList) {
            copyAssetModule(
                moduleInfo.modulePath, if (moduleInfo.moduleFileName.isNullOrEmpty()) {
                    File(dirFile, moduleInfo.moduleName).absolutePath
                } else {
                    File(dirFile, moduleInfo.moduleFileName).absolutePath
                }
            )
        }
        // 过滤错误的Module
        val newModuleInfo =
            _moduleInfoList.filter { it.moduleStoreType == ApiModuleInfo.ModuleStoreType.TYPE_LOCAL }

        // 写入新的配置到本地
        val moduleInfoJson = Gson().toJson(newModuleInfo)
        FileWriter(moduleInfoFile).use {
            it.write(moduleInfoJson)
        }
        _moduleInfoList.clear()
    }

    private fun initModuleCode() {
        val unExistsModule = mutableListOf<ApiModuleInfo>()

        for (moduleInfo in _moduleInfoList) {
            val moduleFile = File(moduleInfo.modulePath)
            if (!moduleFile.exists()) {
                unExistsModule.add(moduleInfo)
                continue
            }
            // 添加资源
            try {
                val moduleContext = ApiModuleContext(context, File(moduleInfo.modulePath))
                moduleContextMap[moduleInfo.moduleKey] = moduleContext
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _moduleInfoList.removeAll(unExistsModule)
        // 加载插件代码
        ProxyClassLoader.hookElement(
            _moduleInfoList.map { it.modulePath }.toList(),
            context.classLoader
        )

        for (moduleInfo in _moduleInfoList) {
            loadApiModuleClass(moduleInfo)
        }
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

}