package com.zipper.api.module

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.utils.FileUtil
import com.zipper.core.utils.SpUtil
import java.io.*
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
object ApiModuleManager {

    private const val MODULE_SP_NAME = "sp_api_module"

    private const val ASSETS_MODULE_FILENAME = "api_module.json"

    private const val LOCAL_MODULE_FILENAME = "api_module.json"

    private const val LOCAL_MODULE_DIR = "api_modules"

    private lateinit var context: Context

    private lateinit var spWrapper: SpUtil.SharedPreferencesWrapper

    private val moduleInfoMap: MutableMap<String, IApiModule> = mutableMapOf()

    private val moduleInfoList: MutableList<ApiModuleInfo> = mutableListOf()

    fun initModule(context: Context){
        this.context = context.applicationContext
        spWrapper = SpUtil.instance(MODULE_SP_NAME)
        val moduleInfoFile = File(context.filesDir, LOCAL_MODULE_FILENAME)
        if(!moduleInfoFile.exists()){
            loadAssetsModuleConfig()
            copyAssetsModuleToLocal(moduleInfoFile)
        }
        loadLocalModuleConfig(moduleInfoFile)
    }

    private fun loadAssetsModuleConfig(){
        context.assets.open(ASSETS_MODULE_FILENAME).use {
            moduleInfoList.clear()
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

    private fun loadApiModule(){
        val unExistsModule = mutableListOf<ApiModuleInfo>()
        val classLoader = ModuleClassLoader(javaClass.classLoader!!)

        for (moduleInfo in moduleInfoList){
            val moduleFile = File(moduleInfo.modulePath)
            if(!moduleFile.exists()){
                unExistsModule.add(moduleInfo)
                continue
            }
        }
        moduleInfoList.removeAll(unExistsModule)
        classLoader.addDexPath(moduleInfoList.map { it.modulePath }.toList())
    }

}