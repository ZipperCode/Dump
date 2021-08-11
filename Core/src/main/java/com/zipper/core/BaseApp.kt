package com.zipper.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.zipper.core.plugin.PluginManager
import com.zipper.core.utils.SpUtil
import java.util.*

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BaseApp : Application(), ViewModelStoreOwner {

    companion object{
        lateinit var instance: BaseApp
    }

    private lateinit var mViewModelStore: ViewModelStore

    private lateinit var mAppViewModelProvider: ViewModelProvider

    override fun onCreate() {
        super.onCreate()
        instance = this
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

    open fun <T : ViewModel> getAppViewModel(clazz: Class<T>): T {
        if (!::mAppViewModelProvider.isInitialized) {
            mAppViewModelProvider =
                ViewModelProvider(this, getFactory())
        }
        return mAppViewModelProvider.get(clazz)
    }

    fun getFactory(): ViewModelProvider.AndroidViewModelFactory{
        return ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    }

    fun startStrictMode(){
        if(BuildConfig.DEBUG){

        }
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