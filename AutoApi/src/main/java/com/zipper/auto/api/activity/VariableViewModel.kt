package com.zipper.auto.api.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.api.module.bean.ApiVariableBean
import com.zipper.auto.api.activity.bean.VariableItemBean
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VariableViewModel: ViewModel() {

    private val variableRepository = VariableRepository()

    private val _variableItemList: MutableLiveData<List<VariableItemBean>> = MutableLiveData()

    val variableItemList: LiveData<List<VariableItemBean>> get() = _variableItemList

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun addVariable(variableItemBean: VariableItemBean){
        if(variableItemBean.name.get().isNullOrEmpty() || variableItemBean.value.get().isNullOrEmpty()){
            return
        }
        viewModelScope.launch {
            val apiVariableBean = ApiVariableBean(
                variableItemBean.name.get()!!,
                variableItemBean.value.get()!!,
                variableItemBean.remarks.get() ?: "",
                variableItemBean.isGlobal.get(),
                false,
                emptyList()
            )
            variableRepository.saveVariable(apiVariableBean)

            refresh()
        }
    }

    fun updateVariable(variableItemBean: VariableItemBean){
        if(variableItemBean.name.get().isNullOrEmpty() || variableItemBean.value.get().isNullOrEmpty()){
            return
        }

        viewModelScope.launch {
            variableRepository.getVariable(variableItemBean.name.get()!!)?.run {
                val newBean = copy(
                    variableItemBean.name.get()!!,
                    variableItemBean.value.get()!!,
                    variableItemBean.remarks.get() ?: "",
                    variableItemBean.isGlobal.get(),
                    isBan,
                    usedModule
                )

                variableRepository.saveVariable(newBean)
            }
            refresh()
        }
    }

    suspend fun checkVariableExists(name: String): Boolean{
        return variableRepository.checkVariableExists(name)
    }


    private suspend fun refresh(){
        variableRepository.apiVariableBeanFlow.collect {
            _variableItemList.value = it.map { value ->
                VariableItemBean.convert(value)
            }.toList()
        }
    }

}