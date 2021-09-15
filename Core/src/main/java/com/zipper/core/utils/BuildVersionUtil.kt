package com.zipper.core.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.os.BuildCompat

object BuildVersionUtil {
    @JvmStatic
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
    fun isAtLollipop(): Boolean{
        return Build.VERSION.SDK_INT >= 21
    }

    @JvmStatic
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun isAtLeastN(): Boolean{
        return Build.VERSION.SDK_INT >= 24
    }
}