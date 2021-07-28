package com.zipper.dump.activity

import androidx.lifecycle.*
import com.zipper.dump.repo.ServiceRepo
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/

class DumpViewModel: ViewModel() {

    private val repo: ServiceRepo = ServiceRepo()

    val serviceStatus: LiveData<Boolean> get() = repo.serviceState.asLiveData()

    val serviceCtrlStatus: LiveData<Boolean> get() = repo.serviceCtrlState.asLiveData()

    fun switchServiceStatus(){
        val newValue = !serviceCtrlStatus.value!!
        viewModelScope.launch {
            repo.saveServiceCtrlState(newValue)
        }
    }
}