package com.zipper.core

import android.app.Activity
import android.os.Bundle

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
interface IPlugin {

    fun onApplicationCreate(baseApp: BaseApp)

    fun initModule()

    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)

    fun onActivityStarted(activity: Activity)

    fun onActivityResumed(activity: Activity)

    fun onActivityPaused(activity: Activity)

    fun onActivityStopped(activity: Activity)

    fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)

    fun onActivityDestroyed(activity: Activity)

    fun onMainActivityCreate(activity: Activity)

    fun onMainActivityDestroy(activity: Activity)

    fun onTrimMemory(level: Int)

}