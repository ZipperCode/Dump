package com.zipper.plugin.dump.fragment

import android.content.Context
import androidx.lifecycle.*
import com.zipper.core.BaseApp
import com.zipper.plugin.dump.bean.AppsInfo
import com.zipper.plugin.dump.repo.AppsRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsViewModel : ViewModel() {

    private val appsRepo: AppsRepo = AppsRepo()

    val appsData: MutableLiveData<MutableList<AppsInfo>> = MutableLiveData(mutableListOf())

    val searchData: MutableLiveData<String> = MutableLiveData("")

    val serviceStatus: LiveData<Boolean> = MutableLiveData(false)

    val applyAll:MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            val d = appsRepo.getInstallApks(BaseApp.instance)
            appsData.postValue(d)
        }
    }

    suspend fun getPackages(context: Context) {
        appsRepo.loadAppInfo(context).collect {
//            appsData.value = it
        }
    }

}