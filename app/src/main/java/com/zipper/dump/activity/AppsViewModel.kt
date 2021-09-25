package com.zipper.dump.activity

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.zipper.core.BaseApp
import com.zipper.core.utils.L
import com.zipper.dump.App
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.repo.AppsRepo
import com.zipper.dump.repo.ServiceRepo
import com.zipper.dump.utils.AccessibilityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.util.*
import kotlin.collections.ArrayList

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsViewModel : ViewModel() {

    private val appsRepo: AppsRepo = AccessibilityHelper.appRepo

    private val serviceRepo: ServiceRepo = AccessibilityHelper.serviceRepo

    /**
     * 安装应用
     */
    val appsData: MutableLiveData<MutableList<AppsInfo>> = MutableLiveData(mutableListOf())

    /**
     * 搜索文字
     */
    val searchData: MutableLiveData<String> = MutableLiveData("")

    /**
     * 无障碍服务开启状态
     */
    val serviceStatus: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * 全部应用的状态
     */
    val applyAllStatus: MutableLiveData<Boolean> = MutableLiveData(false)

    val showLoadingState:MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            showLoadingState.value = true
            serviceRepo.serviceState.collect {
                serviceStatus.postValue(it)
            }
            var pksSet: Set<String> = setOf()
            appsRepo.savePksInfo.collect {
                pksSet = it
            }

            appsRepo.getInstall(BaseApp.instance).collect {
                if (pksSet.contains(it.pks)){
                    it.accessibilityEnable.set(true)
                }
                appsData.value?.add(it)
                appsData.postValue(appsData.value)
            }
            applyAllStatus.value = pksSet.size == appsData.value?.size ?: 0
            showLoadingState.value = false
        }
    }

    fun applyAll(value:Boolean){
        appsData.value?.forEach {
            it.accessibilityEnable.set(value)
        }
    }

    fun applyItem(item: AppsInfo){
        val checkValue = item.accessibilityEnable.get()
        appsRepo.saveOrRemoveAppPks(item.pks,checkValue)
    }

    fun checkAppAll(checkCallback: (Boolean) -> Unit){
        viewModelScope.launch {
            appsRepo.savePksInfo.collect {
                checkCallback(it.size == appsData.value?.size ?: 0)
            }
        }
    }
}