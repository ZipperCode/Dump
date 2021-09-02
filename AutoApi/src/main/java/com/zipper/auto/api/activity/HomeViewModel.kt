package com.zipper.auto.api.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zipper.auto.api.activity.bean.TaskInfoBean

class HomeViewModel: ViewModel() {

    private val _taskInfoList: MutableLiveData<List<TaskInfoBean>> = MutableLiveData()

    val taskInfoList: LiveData<List<TaskInfoBean>> get() = _taskInfoList

    fun requestData() {
        _taskInfoList.value = listOf(
            TaskInfoBean(0, "1"),
            TaskInfoBean(0, "2"),
            TaskInfoBean(0, "3"),
            TaskInfoBean(0, "4"),
            TaskInfoBean(0, "5")
        )
    }
}