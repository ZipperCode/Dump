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

    private val appsRepo: AppsRepo = AppsRepo()

    val appsData: MutableLiveData<MutableList<AppsInfo>> = MutableLiveData(mutableListOf())

    init {
        viewModelScope.launch {
            val d = appsRepo.getInstallApks(BaseApp.instance)
            appsData.value = d
        }
    }

    suspend fun getPackages(context: Context) {
        appsRepo.loadAppInfo(context).collect {
//            appsData.value = it
        }
    }

}