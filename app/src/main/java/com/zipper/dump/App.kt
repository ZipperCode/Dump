package com.zipper.dump

import android.content.Context
import com.zipper.core.BaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        mAppContext = this
    }

    companion object {
        val mMainCoroutinesScope = CoroutineScope(Dispatchers.Main)
        val mIoCoroutinesScope = CoroutineScope(Dispatchers.IO)
        lateinit var mAppContext: Context;
    }
}