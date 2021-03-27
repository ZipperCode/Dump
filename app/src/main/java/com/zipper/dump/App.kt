package com.zipper.dump

import android.app.Application
import android.content.Context
import com.zipper.dump.utils.AccessibilityHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        mAppContext = this;
    }

    companion object {
        val mMainCoroutinesScope = CoroutineScope(Dispatchers.Main);
        val mIoCoroutinesScope = CoroutineScope(Dispatchers.IO);
        lateinit var mAppContext: Context;
    }
}