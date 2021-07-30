package com.zipper.dump.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.repo.AppsRepo
import com.zipper.dump.repo.ServiceRepo
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsViewModel : ViewModel() {

    private val appsRepo: AppsRepo = AppsRepo()

    private val serviceRepo = ServiceRepo()

    val appsData: LiveData<List<AppsInfo>> = appsRepo.appsListData.asLiveData()

    init {

    }

    suspend fun getPackages(context: Context) = appsRepo.getInstallApks(context)
}