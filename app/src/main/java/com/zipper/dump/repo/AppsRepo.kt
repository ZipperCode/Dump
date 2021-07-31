package com.zipper.dump.repo

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import com.zipper.core.utils.SpUtil
import com.zipper.dump.App
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
    @Deprecated("ss")
    private val _appsListData = MutableStateFlow<List<AppsInfo>>(mutableListOf())

    @Deprecated("ss")
    val appsListData: Flow<List<AppsInfo>> get() = _appsListData

    private val _savePksInfo = MutableStateFlow(mutableSetOf<String>())

    val savePksInfo: Flow<Set<String>> get() = _savePksInfo

    val floatPermission:Flow<Boolean> = flow {
        emit(FloatWindow.checkPermission(App.mAppContext, false))
    }

    suspend fun loadSaveDumpPksInfo() = withContext(Dispatchers.IO) {
        val result = SpUtil.instance(SpHelper.SP_NAME).get(SpHelper.SP_SAVE_PKS_KEY, emptySet<String>())
        _savePksInfo.emit(result.toMutableSet())
    }

    suspend fun putSaveDumpPksInfo(pksInfo: Set<String>) = withContext(Dispatchers.IO)  {
        _savePksInfo.emit(pksInfo.toMutableSet())
        SpUtil.instance(SpHelper.SP_NAME).put(SpHelper.SP_SAVE_PKS_KEY, pksInfo)
        loadSaveDumpPksInfo()
    }

    suspend fun putSaveDumpPksInfo(pksInfo: Collection<String>) = withContext(Dispatchers.IO)  {
        _savePksInfo.value.addAll(pksInfo)
        _savePksInfo.emit(_savePksInfo.value)
        SpUtil.instance(SpHelper.SP_NAME).put(SpHelper.SP_SAVE_PKS_KEY, _savePksInfo.value)
        loadSaveDumpPksInfo()
    }

    suspend fun putSaveDumpPksInfo(pkInfo: String, isDel: Boolean = false) = withContext(Dispatchers.IO)  {
        if(isDel){
            _savePksInfo.value.remove(pkInfo)
        }else{
            _savePksInfo.value.add(pkInfo)
        }
        _savePksInfo.emit(_savePksInfo.value)
        SpUtil.instance(SpHelper.SP_NAME).put(SpHelper.SP_SAVE_PKS_KEY, _savePksInfo.value)
        loadSaveDumpPksInfo()
    }

    suspend fun getInstallApks(context: Context) : List<AppsInfo> = withContext(Dispatchers.IO){
        return@withContext context.packageManager.getInstalledApplications(0).map {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(it.packageName)
            val launchIntentForPackage =
                context.packageManager.getLaunchIntentForPackage(it.packageName)
            if (launchIntentForPackage != null) {
                val icon = it.loadIcon(context.packageManager)
                val name = context.packageManager.getApplicationLabel(it)
                if (icon != null && !TextUtils.isEmpty(name)) {
                    return@map AppsInfo(
                        icon,
                        name.toString(),
                        it.packageName,
                        ObservableBoolean(false)
                    )
                }
            }
            null
        }.filterNotNull().toList()
    }

    suspend fun firstInStatus(): Boolean = withContext(Dispatchers.IO)  {
        return@withContext SpUtil.instance(SpHelper.SP_NAME).get(SpHelper.SP_FIRST_OPENED_KEY, false)
    }

    suspend fun saveFirstInStatus(value: Boolean) = withContext(Dispatchers.IO)  {
        SpUtil.instance(SpHelper.SP_NAME).put(SpHelper.SP_FIRST_OPENED_KEY, value)
    }
}