package com.zipper.dump.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zipper.dump.repo.ServiceRepo

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class SettingViewModel: ViewModel() {

    private val repo: ServiceRepo = ServiceRepo()

    val serviceStatus: LiveData<Boolean> get() = repo.serviceState.asLiveData()

    val flowPermissionStatus: LiveData<Boolean> = MutableLiveData(false)

    val autoBootPermissionStatus: LiveData<Boolean> = MutableLiveData(false)

}