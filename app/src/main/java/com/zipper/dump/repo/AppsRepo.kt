package com.zipper.dump.repo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import com.zipper.core.BaseApp
import com.zipper.core.utils.SpUtil
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.utils.SpHelper
import com.zipper.dump.view.FloatWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 *  @author zipper
 *  @date 2021-07-29
 *  @description
 **/
class AppsRepo {

    val savePksInfo: Flow<Set<String>> = flow {
        emit(loadSaveDumpPksInfo())
    }.flowOn(Dispatchers.IO)

    val floatPermission: Flow<Boolean> = flow {
        emit(FloatWindow.checkPermission(BaseApp.instance, false))
    }

    fun loadSaveDumpPksInfo(): Set<String>  {
        return SpUtil.instance(SpHelper.SP_NAME)
            .get<Set<String>>(SpHelper.SP_SAVE_PKS_KEY, emptySet())
    }


    suspend fun getInstallApks(context: Context): MutableList<AppsInfo> = withContext(Dispatchers.IO) {
        val appsList: MutableList<AppsInfo> = mutableListOf()
        val installPackageList =
            context.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)

        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        for (pks in installPackageList) {
            resolveIntent.setPackage(pks.packageName)
            val queryIntentActivities =
                context.packageManager.queryIntentActivities(resolveIntent, 0)

            if (queryIntentActivities.size > 0) {
                val first = queryIntentActivities.first()

                val icon = first.loadIcon(context.packageManager)
                val name = context.packageManager.getApplicationLabel(pks.applicationInfo)
                if (icon != null && !TextUtils.isEmpty(name)) {
                    val appsInfo = AppsInfo(
                        icon,
                        name.toString(),
                        pks.packageName,
                        ObservableBoolean(false)
                    )
                    appsList.add(appsInfo)
                }
            }

        }
        return@withContext appsList
    }

    suspend fun getInstall(context: Context) = flow<AppsInfo> {
        context.packageManager.getInstalledApplications(0).forEach { appInfo ->
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(appInfo.packageName)
            val launchIntentForPackage =
                context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
            if (launchIntentForPackage != null) {
                val icon = appInfo.loadIcon(context.packageManager)
                val name = context.packageManager.getApplicationLabel(appInfo)
                if (icon != null && !TextUtils.isEmpty(name)) {
                    emit(
                        AppsInfo(
                            icon,
                            name.toString(),
                            appInfo.packageName,
                            ObservableBoolean(false)
                        )
                    )
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun firstInStatus(): Boolean = withContext(Dispatchers.IO) {
        return@withContext SpUtil.instance(SpHelper.SP_NAME)
            .get(SpHelper.SP_FIRST_OPENED_KEY, false)
    }

    suspend fun saveFirstInStatus(value: Boolean) = withContext(Dispatchers.IO) {
        SpUtil.instance(SpHelper.SP_NAME).put(SpHelper.SP_FIRST_OPENED_KEY, value)
    }

    fun saveAppPksAll(pksSet: Set<String>){
        SpUtil.instance(SpHelper.SP_NAME).putAsync(SpHelper.SP_SAVE_PKS_KEY, pksSet)
    }

    fun saveOrRemoveAppPks(pks: String, isSave: Boolean){
        val loadSaveDumpPksInfo = HashSet(loadSaveDumpPksInfo())
        if(isSave){
            loadSaveDumpPksInfo.add(pks)
        }else{
            loadSaveDumpPksInfo.remove(pks)
        }
        saveAppPksAll(loadSaveDumpPksInfo)
    }

}