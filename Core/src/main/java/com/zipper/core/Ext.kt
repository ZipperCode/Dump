package com.zipper.core

import java.text.SimpleDateFormat
import java.util.*

fun Long.format(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    return try {
        simpleDateFormat.format(Date(this * 1000))
    } catch (e: Exception) {
        "0000-00-00 00:00:00"
    }
}
