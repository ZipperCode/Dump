package com.zipper.auto.api.activity

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zipper.auto.api.activity.bean.TaskInfoBean
import com.zipper.auto.api.activity.bean.VariableBean
import com.zipper.core.utils.L

class VariableViewModel: ViewModel() {

    private val _variableList: MutableLiveData<MutableList<VariableBean>> = MutableLiveData()

    val variableList: LiveData<MutableList<VariableBean>> get() = _variableList

    fun requestData() {
        _variableList.value = mutableListOf(
            VariableBean(ObservableField("1"), ObservableField("1")),
            VariableBean(ObservableField("2"), ObservableField("2"))
        )
    }

    fun containAndAdd(variableBean: VariableBean){
        if(variableBean.name.get().isNullOrEmpty() || variableBean.value.get().isNullOrEmpty()){
            return
        }

        val variables = _variableList.value ?: mutableListOf()
        if(!variables.contains(variableBean)){
            variables.add(variableBean)
            _variableList.value = variables
        }
    }
}