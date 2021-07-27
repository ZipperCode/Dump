package com.zipper.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BaseApp : Application() {

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

        val mActivityList = LinkedList<Activity>()

        var activityCount: Int = 0

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            PluginManager.onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            PluginManager.onActivityStarted(activity)
            activityCount++
        }

        override fun onActivityResumed(activity: Activity) {
            PluginManager.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            PluginManager.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            PluginManager.onActivityStopped(activity)
            activityCount--
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            PluginManager.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            PluginManager.onActivityDestroyed(activity)
        }

    }
}