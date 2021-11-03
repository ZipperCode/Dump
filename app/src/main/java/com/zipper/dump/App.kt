package com.zipper.dump

import android.annotation.SuppressLint
import android.content.Context
import com.zipper.core.BaseApp
import com.zipper.core.fragment.FragmentNavConfigHelper
import com.zipper.core.utils.ThemeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.R.attr.name
import android.util.Base64
import android.util.Log
import com.zipper.dump.utils.Name
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
//        ThemeHelper.applyTheme(ThemeHelper.ThemeMode.DARK_MODE)
        val a = 1
        val toByteArray =
            packageManager.getPackageInfo("com.ldzs.zhangxin", 64).signatures[0].toByteArray()

        Log.d("AAAAAB",  Base64.encodeToString(toByteArray, 0))
        Log.d("AAAAA", Name.parseSignature(toByteArray,a.toChar()))
        val aa = a % '\n'.toInt()
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