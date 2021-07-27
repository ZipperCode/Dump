package com.zipper.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import java.util.*

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


object LaunchUtil {

    fun isHuaWei(): Boolean {
        return if (Build.BRAND == null) {
            false;
        } else {
            (Build.BRAND.toLowerCase(Locale.ROOT) == "huawei"
                    || Build.BRAND.toLowerCase(Locale.ROOT) == "honor")
        }
    }

    fun toHuaWeiManager(context: Context) {
        context.startActivity(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
        )
    }

    fun isXiaoMi(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "xiaomi"
    }

    fun toXiaoMiManager(context: Context) {
        context.startActivity(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        );
    }

    fun isOppo(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "oppo";
    }


    fun goOPPOManager(context: Context) {
        if (!context.startActivity("com.coloros.phonemanager")
            || !context.startActivity("com.oppo.safe")
            || !context.startActivity("com.coloros.oppoguardelf")
            || !context.startActivity("com.coloros.safecenter")
        ) {
            print("success")
        }
    }

    fun isVivo(): Boolean{
        return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "vivo"
    }

    fun toVivoManager(context: Context){
        context.startActivity("com.iqoo.secure")
    }

    fun isMeiZu(): Boolean{
        return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "meizu"
    }

    fun toMeiZuManager(context: Context){
        context.startActivity("com.meizu.safe")
    }

    fun isSamsung(): Boolean{
        return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "samsung"
    }

    fun toSamsungManager(context: Context){
        if(!context.startActivity("com.samsung.android.sm_cn")){
            context.startActivity("com.samsung.android.sm")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun toIgnoreBatteryOptimization(context: Context){
        try{
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}