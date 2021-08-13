package com.zipper.core.ext

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/

fun Context.startActivity(packageName: String): Boolean {
    return try {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.startActivity(packageName: String, activityName: String): Boolean {
    return try {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.dp2px(dp: Int): Int{
    return (resources.displayMetrics.density * dp + 0.5).toInt()
}

fun Context.sp2Px(sp: Int) : Int{
    return (resources.displayMetrics.scaledDensity * sp + 0.5).toInt()
}

fun Context.notificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager