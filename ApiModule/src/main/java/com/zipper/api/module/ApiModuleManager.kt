package com.zipper.api.module

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.api.module.bean.ApiModuleInfo
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

    private const val LOCAL_MODULE_FILENAME = "api_module.json"

    private const val LOCAL_MODULE_DIR = "api_modules"
    /* 应用上下文 */
    private lateinit var context: Context
    /* sp存储类 */
    private lateinit var spWrapper: SpUtil.SharedPreferencesWrapper
    /* 模块接口 */
    private val moduleMap: MutableMap<String, IApiModule> = mutableMapOf()
    /* 模块配置信息列表 */
    private val moduleInfoList: MutableList<ApiModuleInfo> = mutableListOf()
    /* 模块上下文 */
    private val moduleContextMap: MutableMap<String, ApiModuleContext> = mutableMapOf()
    /* 模块运行状态 */
    private val moduleRunningStatusMap: MutableMap<String, Boolean> = mutableMapOf()

    fun initModule(context: Context){
        this.context = context.applicationContext
        spWrapper = SpUtil.instance(MODULE_SP_NAME)
        val moduleInfoFile = File(context.filesDir, LOCAL_MODULE_FILENAME)
        if(!moduleInfoFile.exists()){
            loadAssetsModuleConfig()
            copyAssetsModuleToLocal(moduleInfoFile)
        }
        loadLocalModuleConfig(moduleInfoFile)
        initModuleCode()
    }

    private fun loadAssetsModuleConfig(){
        context.assets.open(ASSETS_MODULE_FILENAME).use {
            moduleInfoList.clear()
            moduleMap.clear()
            val moduleList: List<ApiModuleInfo> = Gson().fromJson(InputStreamReader(it),object :TypeToken<List<ApiModuleInfo>>(){}.type)
            moduleInfoList.addAll(moduleList)
        }
    }

    private fun loadLocalModuleConfig(moduleInfoFile: File){
        FileReader(moduleInfoFile).use {
            moduleInfoList.clear()
            val moduleList: List<ApiModuleInfo> = Gson().fromJson(it,object :TypeToken<List<ApiModuleInfo>>(){}.type)
            moduleInfoList.addAll(moduleList)
        }
    }

    private fun copyAssetsModuleToLocal(moduleInfoFile: File){
        val dirFile = File(context.filesDir, LOCAL_MODULE_DIR)
        if(!dirFile.exists()){
            dirFile.mkdirs()
        }
        for (moduleInfo in moduleInfoList){
            try{
                context.assets.open(moduleInfo.modulePath).use {
                    val moduleFile = if(moduleInfo.moduleFileName.isNullOrEmpty()) {
                        File(dirFile,moduleInfo.moduleName)
                    }else{
                        File(dirFile,moduleInfo.moduleFileName)
                    }
                    if(!moduleFile.exists()){
                        moduleFile.createNewFile()
                    }
                    FileUtil.copyFile(it,FileOutputStream(moduleFile))
                    moduleInfo.moduleStoreType = ApiModuleInfo.ModuleStoreType.TYPE_LOCAL
                    moduleInfo.modulePath = moduleFile.absolutePath
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        // 过滤错误的Module
        val newModuleInfo = moduleInfoList.filter { it.moduleStoreType == ApiModuleInfo.ModuleStoreType.TYPE_LOCAL }

        // 写入新的配置到本地
        val moduleInfoJson = Gson().toJson(newModuleInfo)
        FileWriter(moduleInfoFile).use {
            it.write(moduleInfoJson)
        }
        moduleInfoList.clear()
    }

    private fun initModuleCode(){
        val unExistsModule = mutableListOf<ApiModuleInfo>()
        val classLoader = ProxyClassLoader(javaClass.classLoader!!)

        for (moduleInfo in moduleInfoList){
            val moduleFile = File(moduleInfo.modulePath)
            if(!moduleFile.exists()){
                unExistsModule.add(moduleInfo)
                continue
            }
            // 添加资源
            try {
                val moduleContext = ApiModuleContext(context,File(moduleInfo.modulePath))
                val packageName = moduleContext.packageName
                moduleContextMap[packageName] = moduleContext
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        moduleInfoList.removeAll(unExistsModule)
        // 加载插件代码
        classLoader.addDexPath(moduleInfoList.map { it.modulePath }.toList())

        for (moduleInfo in moduleInfoList){
            loadApiModuleClass(moduleInfo)
        }
    }

    fun checkModule(key: String): Boolean{
        try {
            val moduleInfo = moduleInfoList.find { it.moduleKey == key }

            if(moduleInfo == null){
                L.d(TAG,"未找到 $key 对应的模块信息")
                return false
            }
            var module = moduleMap[key]
            if(module == null){
                L.d(TAG,"未找到 $key 对应的模块 尝试重新加载模块")
                module = loadApiModuleClass(moduleInfo)
            }

            if(module == null){
                L.d(TAG,"重新加载 $key 对应的模块后仍未找到模块")
                return false
            }
            return true
        }catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }

    fun callModuleMain(key:String, variable: Map<String, String>){
        if(!checkModule(key)){
            return
        }
        val module = moduleMap[key]!!
        try {
            moduleRunningStatusMap[key] = true
            // 注入变量
            module.setVariable(variable)
            // 执行模块主代码
            module.execute()
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            moduleRunningStatusMap[key] = false
        }
    }

    fun checkModuleIsRun(key: String): Boolean{
        if(!checkModule(key)){
           return false
        }
        return moduleRunningStatusMap[key] ?: false
    }


    private fun loadApiModuleClass(moduleInfo: ApiModuleInfo): IApiModule?{
        var resultModule: IApiModule? = null
        try {
            val moduleContext = moduleContextMap[moduleInfo.moduleKey]

            if(moduleContext == null){
                L.d(TAG,"the ${moduleInfo.moduleName} moduleContext == null")
                return resultModule
            }
            val moduleImplClass = Class.forName(moduleInfo.modulePath)
            val moduleConstructor = moduleImplClass.getConstructor(Context::class.java)
            resultModule = moduleConstructor.newInstance(moduleContext) as IApiModule
            moduleMap[moduleInfo.moduleKey] = resultModule
        }catch (e: Exception){
            e.printStackTrace()
        }
        return resultModule
    }

}