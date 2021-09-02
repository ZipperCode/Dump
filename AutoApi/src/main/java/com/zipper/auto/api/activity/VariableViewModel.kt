package com.zipper.auto.api.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zipper.auto.api.activity.bean.TaskInfoBean
import com.zipper.auto.api.activity.bean.VariableBean

class VariableViewModel: ViewModel() {

    private val _variableList: MutableLiveData<List<VariableBean>> = MutableLiveData()

    val variableList: LiveData<List<VariableBean>> get() = _variableList

    fun requestData() {
        _variableList.value = listOf(

        )
    }
}