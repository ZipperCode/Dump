package com.zipper.core

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BasePlugin : IPlugin {

    companion object{
        lateinit var app: BaseApp
    }

    override fun onApplicationCreate(baseApp: BaseApp) {
        app = baseApp
        Log.d(javaClass.simpleName, "onApplicationCreate")
    }

    override fun initModule() {
        Log.d(javaClass.simpleName, "initModule")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(javaClass.simpleName, "onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(javaClass.simpleName, "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(javaClass.simpleName, "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(javaClass.simpleName, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(javaClass.simpleName, "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(javaClass.simpleName, "onActivityDestroyed")
    }

    override fun onTrimMemory(level: Int) {
        Log.d(javaClass.simpleName, "onTrimMemory")
    }
}