package com.example.dumping.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import com.example.dumping.bean.AppInfo
import java.util.*


object AppUtils {
    /**
     * 检查无障碍服务是否开启
     *
     * @param context     当前上下文
     * @param serviceName 服务名：包名/类名 可能包含全类名也可能不包含
     * @return 开启为true
     */
    fun checkAccessibilityOn1(context: Context?, serviceName: String?): Boolean {
        if (context == null || serviceName == null) {
            return false
        }
        var isStarted = false
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledAccessibilityServiceList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (i in enabledAccessibilityServiceList.indices) {
            if (enabledAccessibilityServiceList[i].id == serviceName) {
                isStarted = true
                break
            }
        }
        return isStarted
    }

    fun checkAccessibilityOn2(context: Context?, serviceName: String?): Boolean {
        if (context == null || serviceName == null) {
            return false
        }
        var ok = 0
        try {
            ok = Settings.Secure.getInt(context.applicationContext.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            // TODO Ignore
        }
        val ms = TextUtils.SimpleStringSplitter(':')
        if (ok == 1) {
            val settingValue = Settings.Secure.getString(context.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                ms.setString(settingValue)
                while (ms.hasNext()) {
                    val accessibilityService = ms.next()
                    if (accessibilityService.equals(serviceName, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }


    fun isAccessibilitySettingsOn(mContext: Context, clazz: Class<out AccessibilityService?>): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.applicationContext.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(mContext.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getPackages(context: Context): List<PackageInfo> {
        val packageManager = context.packageManager
        return packageManager.getInstalledPackages(0)
    }


    @SuppressLint("QueryPermissionsNeeded")
    fun getLaunch(context: Context, list: MutableList<AppInfo>){
        val pks = getPackages(context)
        pks.forEach {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(it.packageName)
            val resolveInfo = context.packageManager.queryIntentActivities(intent, 0)
            if(resolveInfo.size > 0){
                val icon = it.applicationInfo.loadIcon(context.packageManager)
                val name = context.packageManager.getApplicationLabel(it.applicationInfo)
                if (!TextUtils.isEmpty(name) and (icon != null)){
                    list.add(AppInfo(icon, name.toString(),
                            it.packageName))
                }
            }
        }
    }

    fun getLaunch(context: Context, map: MutableMap<String, String>){
        val pks = getPackages(context)
        pks.forEach {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(it.packageName)
            val launchIntent = context.packageManager.getLaunchIntentForPackage(it.packageName)
            launchIntent?.run {
                val packageName = component?.packageName?:""
                val launchActivity = component?.className?:""
                if (!TextUtils.isEmpty(packageName) and !TextUtils.isEmpty(launchActivity)) {
                    map[packageName] = launchActivity
                }
            }
        }
    }

}