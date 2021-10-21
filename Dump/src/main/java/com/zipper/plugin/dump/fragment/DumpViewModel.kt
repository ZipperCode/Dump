package com.zipper.plugin.dump.fragment

import androidx.lifecycle.*
import com.zipper.core.utils.L
import com.zipper.plugin.dump.repo.ServiceRepo
import com.zipper.plugin.dump.util.AccessibilityHelper
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
        if(!repo.serviceState.value){
            L.d("无障碍服务还未开启")
            return
        }

        val newValue = !repo.serviceCtrlState.value
        viewModelScope.launch {
            repo.saveServiceCtrlState(newValue)
        }
    }
}