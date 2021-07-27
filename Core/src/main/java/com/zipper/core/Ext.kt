package com.zipper.core

import android.app.ActivityManager
import android.content.Context
import android.text.Html
import android.text.Spanned
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

fun String.toHtml():Spanned{
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun Context.isMainProcess(): Boolean{
    val pid = android.os.Process.myPid()
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    var result = false
    for (process in am.runningAppProcesses){
        if(pid == process.pid){
            result = true
            break
        }
    }
    return result
}