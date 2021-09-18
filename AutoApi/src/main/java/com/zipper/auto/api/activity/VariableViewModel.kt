package com.zipper.auto.api.activity

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zipper.auto.api.activity.bean.VariableItemBean

class VariableViewModel: ViewModel() {

    private val _variableItemList: MutableLiveData<MutableList<VariableItemBean>> = MutableLiveData()

    val variableItemList: LiveData<MutableList<VariableItemBean>> get() = _variableItemList

    fun requestData() {
        _variableItemList.value = mutableListOf(
            VariableItemBean(ObservableField("1"), ObservableField("1")),
            VariableItemBean(ObservableField("2"), ObservableField("2"))
        )
    }

    fun updateOrAdd(variableItemBean: VariableItemBean){
        if(variableItemBean.name.get().isNullOrEmpty() || variableItemBean.value.get().isNullOrEmpty()){
            return
        }

        val variables = _variableItemList.value ?: mutableListOf()
        if(!variables.contains(variableItemBean)){
            variables.add(variableItemBean)
            _variableItemList.value = variables
        }
    }

}