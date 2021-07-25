package com.zipper.core

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import java.lang.Exception

object PluginManager: IPlugin {

    const val PLUGIN_HEAD = "plugin_"

    private val mModuleMap: MutableMap<String, IPlugin> = mutableMapOf()

    private var mMetaData: Bundle? = null

    fun init(context: Context){
        var appInfo: ApplicationInfo? = null
        val packageManager = context.applicationContext.packageManager
        try{
            appInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        }catch (e: Exception){
            e.printStackTrace()
        }
        if(appInfo != null){
            mMetaData = appInfo.metaData
            val pluginNameKeys = mMetaData!!.keySet().filter { it.startsWith(PLUGIN_HEAD) }.toList()
            for (pluginNameKey in pluginNameKeys){
                try {
                    val pluginClassValue = mMetaData!![pluginNameKey] as String
                    val instance:IPlugin  = Class.forName(pluginClassValue).getConstructor().newInstance() as IPlugin
                    mModuleMap[pluginNameKey] = instance
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun <T> getPlugin(name: String): T?{
        return try {
            mModuleMap[name] as? T
        }catch (e: Exception){
            null
        }
    }

    override fun onApplicationCreate(baseApp: BaseApp) {
        mModuleMap.values.forEach { it.onApplicationCreate(baseApp) }
    }

    override fun initModule() {
        mModuleMap.values.forEach { it.initModule() }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mModuleMap.values.forEach { it.onActivityCreated(activity, savedInstanceState) }
    }

    override fun onActivityStarted(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityStarted(activity) }
    }

    override fun onActivityResumed(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityResumed(activity) }
    }

    override fun onActivityPaused(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityPaused(activity) }
    }

    override fun onActivityStopped(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityStarted(activity) }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        mModuleMap.values.forEach { it.onActivitySaveInstanceState(activity,outState) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityDestroyed(activity) }
    }

    override fun onTrimMemory(level: Int) {
        mModuleMap.values.forEach { it.onTrimMemory(level) }
    }
}