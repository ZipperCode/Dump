package com.zipper.core.plugin

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.zipper.core.BaseApp

object PluginManager : IPlugin, IAppStatusChangedListener {

    private const val PLUGIN_HEAD = "Plugin_"

    private val mModuleMap: MutableMap<String, IPlugin> = mutableMapOf()

    val moduleMap:Map<String,IPlugin> get() = mModuleMap

    private var mMetaData: Bundle? = null

    fun init(context: Context) {
        var appInfo: ApplicationInfo? = null
        val packageManager = context.packageManager
        try {
            appInfo =
                packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (appInfo != null) {
            mMetaData = appInfo.metaData
            val pluginNameKeys = mMetaData!!.keySet().filter { it.startsWith(PLUGIN_HEAD) }.toList()
            for (pluginNameKey in pluginNameKeys) {
                try {
                    val pluginClassValue = mMetaData!![pluginNameKey] as String
                    val instance: IPlugin =
                        Class.forName(pluginClassValue).getConstructor().newInstance() as IPlugin
                    mModuleMap[pluginNameKey] = instance
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    inline fun <reified T> getPlugin(name: String): T? {
        return try {
            moduleMap[name] as? T
        } catch (e: Exception) {
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
        mModuleMap.values.forEach { it.onActivitySaveInstanceState(activity, outState) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        mModuleMap.values.forEach { it.onActivityDestroyed(activity) }
    }

    override fun onMainActivityCreate(activity: Activity) {
        mModuleMap.values.forEach { it.onMainActivityCreate(activity) }
    }

    override fun onMainActivityDestroy(activity: Activity) {
        mModuleMap.values.forEach { it.onMainActivityDestroy(activity) }
    }

    override fun onTrimMemory(level: Int) {
        mModuleMap.values.forEach { it.onTrimMemory(level) }
    }

    override fun onForeground() {


        mModuleMap.values.forEach {
            if (it is IAppStatusChangedListener) {
                it.onForeground()
            }
        }
    }

    override fun onBackground() {
        mModuleMap.values.forEach {
            if (it is IAppStatusChangedListener) {
                it.onBackground()
            }
        }
    }
}