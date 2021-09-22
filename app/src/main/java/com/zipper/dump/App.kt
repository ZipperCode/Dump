package com.zipper.dump

import android.annotation.SuppressLint
import android.content.Context
import com.zipper.core.BaseApp
import com.zipper.core.fragment.FragmentNavConfigHelper
import com.zipper.core.utils.ThemeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
//        ThemeHelper.applyTheme(ThemeHelper.ThemeMode.DARK_MODE)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        val mMainCoroutinesScope : CoroutineScope by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            CoroutineScope(Dispatchers.Main)
        }
        val mIoCoroutinesScope: CoroutineScope by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            CoroutineScope(Dispatchers.IO)
        }
    }
}