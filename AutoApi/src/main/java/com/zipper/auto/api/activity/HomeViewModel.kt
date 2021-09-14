package com.zipper.auto.api.activity

import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zipper.auto.api.activity.bean.ModuleTaskBean
import com.zipper.auto.api.activity.bean.TaskInfoBean

class HomeViewModel: ViewModel() {

    private val _taskInfoList: MutableLiveData<List<ModuleTaskBean>> = MutableLiveData()

    val taskInfoList: LiveData<List<ModuleTaskBean>> get() = _taskInfoList

    private val homeRepository = HomeRepository()

    init {

        _taskInfoList.value = listOf(
            ModuleTaskBean("0", ObservableField("123"), ObservableLong(0L)),
            ModuleTaskBean("1", ObservableField("123546"), ObservableLong(0L)),
            ModuleTaskBean("2", ObservableField("234"), ObservableLong(0L)),
            ModuleTaskBean("3", ObservableField("fasdfa"), ObservableLong(0L)),
            ModuleTaskBean("4", ObservableField("fadsf"), ObservableLong(0L))
        )
    }

    fun requestData() {

    }
}