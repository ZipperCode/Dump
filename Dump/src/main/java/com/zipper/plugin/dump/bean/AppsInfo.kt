package com.zipper.plugin.dump.bean

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableBoolean

data class AppsInfo(
    val icon: Drawable,
    val appName: String,
    val pks: String,
    val accessibilityEnable: ObservableBoolean
)
