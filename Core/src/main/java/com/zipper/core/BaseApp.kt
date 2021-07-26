package com.zipper.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BaseApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        PluginManager.init(base)
    }
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        PluginManager.onApplicationCreate(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        PluginManager.onTrimMemory(level)
    }

    private val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            PluginManager.onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            PluginManager.onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            PluginManager.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            PluginManager.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            PluginManager.onActivityStopped(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            PluginManager.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            PluginManager.onActivityDestroyed(activity)
        }

    }
}