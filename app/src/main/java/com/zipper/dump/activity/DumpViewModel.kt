package com.zipper.dump.activity

import androidx.lifecycle.*
import com.zipper.core.DefaultLifecycleObserver
import com.zipper.core.utils.L
import com.zipper.dump.repo.ServiceRepo
import com.zipper.dump.utils.AccessibilityHelper
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/

class DumpViewModel: ViewModel() {

    private val repo: ServiceRepo = AccessibilityHelper.serviceRepo

    val serviceStatus: LiveData<Boolean> get() = repo.serviceState.asLiveData()

    val serviceCtrlStatus: LiveData<Boolean> get() = repo.serviceCtrlState.asLiveData()

    suspend fun refreshServiceState(){
        repo.refreshServiceState()
    }

    fun switchServiceStatus(){
        if(serviceCtrlStatus.value == null){
            L.d("无障碍服务还未开启")
            return
        }

        val newValue = serviceCtrlStatus.value!!
        viewModelScope.launch {
            repo.saveServiceCtrlState(newValue)
        }
    }
}