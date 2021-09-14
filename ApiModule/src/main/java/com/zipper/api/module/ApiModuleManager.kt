package com.zipper.api.module

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.zipper.core.utils.SpUtil
import java.io.File

@SuppressLint("StaticFieldLeak")
object ApiModuleManager {

    private const val MODULE_SP_NAME = "sp_api_module"

    private const val ASSETS_MODULE_FILENAME = "api_module.json"

    private const val LOCAL_MODULE_FILENAME = "api_module.json"

    private lateinit var context: Context

    private lateinit var spWrapper: SpUtil.SharedPreferencesWrapper

    private val moduleInfoMap: MutableMap<String, IApiModule> = mutableMapOf()

    fun initModule(context: Context){
        this.context = context.applicationContext
        spWrapper = SpUtil.instance(MODULE_SP_NAME)
        val moduleFile = File(context.filesDir, LOCAL_MODULE_FILENAME)
        if(!moduleFile.exists()){
            loadAssetsModuleConfig()
        }
    }

    private fun loadAssetsModuleConfig(){
        context.assets.open(ASSETS_MODULE_FILENAME).use {

        }
    }

    fun loadModule(){

    }

}