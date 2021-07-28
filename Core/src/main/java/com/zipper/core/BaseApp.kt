package com.zipper.core

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.zipper.core.utils.SpUtil
import java.util.*

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BaseApp : Application(), ViewModelStoreOwner {

    private lateinit var mViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        SpUtil.init(this)
        mViewModelStore = ViewModelStore()
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        PluginManager.onApplicationCreate(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        PluginManager.onTrimMemory(level)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }

    fun getFactory(): ViewModelProvider.AndroidViewModelFactory{
        return ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    }

    private val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

        val activityList = LinkedList<Activity>()

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            PluginManager.onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            activityList.add(activity)
            PluginManager.onActivityStarted(activity)
            if (activityList.size == 1) {
                PluginManager.onForeground()
            }
        }

        override fun onActivityResumed(activity: Activity) {
            PluginManager.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            PluginManager.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            activityList.remove(activity)
            PluginManager.onActivityStopped(activity)
            if (activityList.size == 0) {
                PluginManager.onBackground()
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            PluginManager.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            PluginManager.onActivityDestroyed(activity)
        }

    }
}