package com.zipper.core

import android.app.Activity
import android.content.ComponentCallbacks2
import android.os.Bundle
import com.zipper.core.utils.L

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description 为模块提供统一接口
 **/
abstract class BasePlugin : IPlugin {

    companion object {
        lateinit var app: BaseApp
    }

    override fun onApplicationCreate(baseApp: BaseApp) {
        app = baseApp
        L.d("app = $baseApp")
    }

    override fun initModule() {
        L.d()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        L.d("activity = $activity savedInstanceState = $savedInstanceState")
    }

    override fun onActivityStarted(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onActivityResumed(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onActivityPaused(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        L.d("activity = $activity outState = $outState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onMainActivityCreate(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onMainActivityDestroy(activity: Activity) {
        L.d("activity = $activity")
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            // 当前手机目前内存吃紧 （后台进程数量少），系统开始根据LRU缓存来清理进程，
            // 而该程序位于LRU缓存列表的最边缘位置，系统会先杀掉该进程，应尽释放一切可以释放的内存。
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> L.e("内存紧-系统将会优先处理此进程")
            // 当前手机目前内存吃紧（后台进程数量少），系统开始根据LRU缓存来清理进程，
            // 而该程序位于LRU缓存列表的中间位置，应该多释放一些内存提高运行效率。
            ComponentCallbacks2.TRIM_MEMORY_MODERATE -> L.e("内存紧-处理LRU中间")
            // 当前手机目前内存吃紧（后台进程数量少），系统开始根据LRU缓存来清理进程，
            // 而该程序位于LRU缓存列表的头部位置，不太可能被清理掉的，
            // 但释放掉一些比较容易恢复的资源能够提高手机运行效率，同时也能保证恢复速度。
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND -> L.e("内存紧-处于LRU头部")
            // 当前应用程序的所有UI界面不可见，一般是用户点击了Home键或者Back键，导致应用的UI界面不可见，
            // 这时应该释放一些UI相关资源，TRIM_MEMORY_UI_HIDDEN是使用频率最高的裁剪等级。
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> L.e("界面不可见了")
            // 表示该进程是前台或可见进程，但是目前手机比较内存十分吃紧（后台及空进程基本被全干掉了），
            // 这时应当尽可能地去释放任何不必要的资源，否则，系统可能会杀掉所有缓存中的进程，并且杀一些本来应当保持运行的进程。
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> L.e("内存紧-处于前台（缓存被清理）")
            // 表示该进程是前台或可见进程，正常运行，一般不会被杀掉，但是目前手机比较吃紧（后台及空进程被全干掉了一大波），
            // 应该去释放掉一些不必要的资源以提升系统性能。
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> L.e("内存紧-处于前台")
            // 表示该进程是前台或可见进程，正常运行，一般不会被杀掉，但是目前手机有些吃紧（后台及空进程存量不多），
            // 系统已经开始清理内存，有必要的话，可以释放一些内存。
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE -> L.e("内存紧-前台-不会被清理")

        }

    }
}