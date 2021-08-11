package com.zipper.core.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.zipper.core.ext.statusBarAdaptive

/**
 *  @author zipper
 *  @date 2021-07-29
 *  @description
 **/
object BarUtil {

    fun transparentStatusBar(activity: Activity) {
        val window = activity.window
        // 设置绘制状态栏背景标志 5.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            // 状态栏图标
            decorView.statusBarAdaptive(true)
        }

        window.statusBarColor = Color.TRANSPARENT

    }
}