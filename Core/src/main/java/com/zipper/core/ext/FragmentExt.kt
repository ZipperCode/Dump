package com.zipper.core.ext

import androidx.fragment.app.Fragment
import com.zipper.core.delegates.AutoClearedValue

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)