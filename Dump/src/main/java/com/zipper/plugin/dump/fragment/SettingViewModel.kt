package com.zipper.plugin.dump.fragment

import androidx.lifecycle.*
import com.zipper.plugin.dump.repo.AppsRepo
import com.zipper.plugin.dump.repo.ServiceRepo
import com.zipper.plugin.dump.util.AccessibilityHelper

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class SettingViewModel: ViewModel() {

    private val repo: ServiceRepo = AccessibilityHelper.serviceRepo
    private val appRepo: AppsRepo = AccessibilityHelper.appRepo

    val serviceStatus: LiveData<Boolean> = repo.serviceState.asLiveData()

    val flowPermissionStatus: LiveData<Boolean> = appRepo.floatPermission.asLiveData()

    val autoBootPermissionStatus: LiveData<Boolean> = MutableLiveData(false)

}