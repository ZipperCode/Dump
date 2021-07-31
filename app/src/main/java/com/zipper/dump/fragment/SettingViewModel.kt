package com.zipper.dump.fragment

import androidx.lifecycle.*
import com.zipper.dump.repo.AppsRepo
import com.zipper.dump.repo.ServiceRepo
import com.zipper.dump.utils.AccessibilityHelper
import kotlinx.coroutines.flow.collect

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