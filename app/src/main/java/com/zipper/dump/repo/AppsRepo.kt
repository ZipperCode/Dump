package com.zipper.dump.repo

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.zipper.dump.bean.AppInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *  @author zipper
 *  @date 2021-07-29
 *  @description
 **/
class AppsRepo {

    private val _appsListData = MutableStateFlow<List<AppInfo>>(mutableListOf())

    val appsListData: Flow<List<AppInfo>> get() = _appsListData

    suspend fun getInstallApks(context: Context) {
        _appsListData.emit(context.packageManager.getInstalledApplications(0).map {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(it.packageName)
            val launchIntentForPackage =
                context.packageManager.getLaunchIntentForPackage(it.packageName)
            if (launchIntentForPackage != null) {
                val icon = it.loadIcon(context.packageManager)
                val name = context.packageManager.getApplicationLabel(it)
                if (icon != null && !TextUtils.isEmpty(name)) {
                    return@map AppInfo(icon, name.toString(), it.packageName)
                }
            }
            null
        }.filterNotNull().toList())
    }
}