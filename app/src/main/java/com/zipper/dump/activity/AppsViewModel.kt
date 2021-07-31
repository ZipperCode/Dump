package com.zipper.dump.activity

import android.content.Context
import androidx.lifecycle.*
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.repo.AppsRepo
import com.zipper.dump.repo.ServiceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.subscribeOn

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsViewModel : ViewModel() {

    private val appsRepo: AppsRepo = AppsRepo()

    val appsData: MutableLiveData<List<AppsInfo>> = MutableLiveData()

    val pks:LiveData<Set<String>> = appsRepo.savePksInfo.asLiveData()

    suspend fun getPackages(context: Context) {
        val apps = appsRepo.getInstallApks(context)
        appsData.value = apps
        appsRepo.loadSaveDumpPksInfo()
    }

}