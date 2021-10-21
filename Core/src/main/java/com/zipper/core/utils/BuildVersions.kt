package com.zipper.core.utils

import androidx.annotation.ChecksSdkIntAtLeast
import android.os.Build

/**
 * @author zhangzhipeng
 * @date 2021/10/21
 */
object BuildVersions {
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    val isAboveP: Boolean
        get() = Build.VERSION.SDK_INT > Build.VERSION_CODES.P
}