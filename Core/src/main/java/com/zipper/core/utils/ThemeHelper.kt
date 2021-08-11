package com.zipper.core.utils

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

/**
 * 暗黑模式设置 需要配置value-night的主题，不然无效果
 */
object ThemeHelper {

    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ThemeMode {
        companion object {
            const val DEFAULT_MODE: Int = 0
            const val LIGHT_MODE: Int = 1
            const val DARK_MODE: Int = 2
        }
    }

    fun applyTheme(@ThemeMode mode: Int = ThemeMode.DEFAULT_MODE) {
        when (mode) {
            ThemeMode.LIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            ThemeMode.DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                )
            }
        }
    }
}