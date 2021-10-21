package com.zipper.plugin.dump.repo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import com.zipper.core.BaseApp
import com.zipper.core.utils.SpUtil
import com.zipper.plugin.dump.Constant
import com.zipper.plugin.dump.bean.AppsInfo

import com.zipper.plugin.dump.view.FloatWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 *  @author zipper
 *  @date 2021-07-29
 *  @description
 **/
class AppsRepo {

    private val _savePksInfo = MutableStateFlow(mutableSetOf<String>())

    val savePksInfo: Flow<Set<String>> get() = _savePksInfo

    val floatPermission: Flow<Boolean> = flow {
        emit(FloatWindow.checkPermission(BaseApp.instance, false))
    }

    suspend fun loadSaveDumpPksInfo() = withContext(Dispatchers.IO) {
        val result =
            SpUtil.instance(Constant.DUMP_SP_NAME).get(Constant.SP_SAVE_PKS_KEY, emptySet<String>())
        _savePksInfo.emit(result.toMutableSet())
    }

    suspend fun putSaveDumpPksInfo(pksInfo: Set<String>) = withContext(Dispatchers.IO) {
        _savePksInfo.emit(pksInfo.toMutableSet())
        SpUtil.instance(Constant.DUMP_SP_NAME).put(Constant.SP_SAVE_PKS_KEY, pksInfo)
        loadSaveDumpPksInfo()
    }

    suspend fun putSaveDumpPksInfo(pksInfo: Collection<String>) = withContext(Dispatchers.IO) {
        _savePksInfo.value.addAll(pksInfo)
        _savePksInfo.emit(_savePksInfo.value)
        SpUtil.instance(Constant.DUMP_SP_NAME).put(Constant.SP_SAVE_PKS_KEY, _savePksInfo.value)
        loadSaveDumpPksInfo()
    }

    suspend fun putSaveDumpPksInfo(pkInfo: String, isDel: Boolean = false) =
        withContext(Dispatchers.IO) {
            if (isDel) {
                _savePksInfo.value.remove(pkInfo)
            } else {
                _savePksInfo.value.add(pkInfo)
            }
            _savePksInfo.emit(_savePksInfo.value)
            SpUtil.instance(Constant.DUMP_SP_NAME).put(Constant.SP_SAVE_PKS_KEY, _savePksInfo.value)
            loadSaveDumpPksInfo()
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
//        return@withContext context.packageManager.getInstalledApplications(PackageManager.GET_ACTIVITIES).map {
//            val intent = Intent(Intent.ACTION_MAIN)
//            intent.addCategory(Intent.CATEGORY_LAUNCHER)
//            intent.setPackage(it.packageName)
//            val launchIntentForPackage =
//                context.packageManager.getLaunchIntentForPackage(it.packageName)
//            if (launchIntentForPackage != null) {
//                val icon = it.loadIcon(context.packageManager)
//                val name = context.packageManager.getApplicationLabel(it)
//                if (icon != null && !TextUtils.isEmpty(name)) {
//                    return@map AppsInfo(
//                        icon,
//                        name.toString(),
//                        it.packageName,
//                        ObservableBoolean(false)
//                    )
//                }
//            }
//            null
//        }.filterNotNull().toList()
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
        return@withContext SpUtil.instance(Constant.DUMP_SP_NAME)
            .get(Constant.SP_FIRST_OPENED_KEY, false)
    }

    suspend fun saveFirstInStatus(value: Boolean) = withContext(Dispatchers.IO) {
        SpUtil.instance(Constant.DUMP_SP_NAME).put(Constant.SP_FIRST_OPENED_KEY, value)
    }

    suspend fun loadAppInfo(context: Context): Flow<List<AppsInfo>> = withContext(Dispatchers.IO) {

        val task1 = async {
            getInstallApks(context)
        }

        val task2 = async {
            loadSaveDumpPksInfo()
        }
        val apps = task1.await()
        task2.await()
        return@withContext savePksInfo
            .combineTransform<Set<String>, List<AppsInfo>, List<AppsInfo>>(
                flow {
                    emit(apps)
                }) { pks, list ->
                list.map {
                    if (pks.contains(it.pks)) {
                        it.accessibilityEnable.set(true)
                    }
                    return@map it
                }
            }
    }
}