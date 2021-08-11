package com.zipper.dump

import com.zipper.core.BaseApp
import com.zipper.core.utils.ThemeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
//        ThemeHelper.applyTheme(ThemeHelper.ThemeMode.DARK_MODE)
    }

    companion object {
        val mMainCoroutinesScope : CoroutineScope by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            CoroutineScope(Dispatchers.Main)
        }
        val mIoCoroutinesScope: CoroutineScope by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            CoroutineScope(Dispatchers.IO)
        }
    }
}