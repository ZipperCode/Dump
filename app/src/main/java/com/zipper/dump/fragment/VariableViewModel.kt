package com.zipper.dump.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.dump.bean.VariableItemBean
import com.zipper.task.module.VariableHelper
import com.zipper.task.module.bean.VariableBean
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VariableViewModel : ViewModel() {

    companion object {
        const val TAG = "VariableViewModel"
    }

    private val variableRepository: VariableRepository = VariableRepository()

    val variableItemList: MutableLiveData<List<VariableItemBean>> = MutableLiveData()

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun addVariable(variableItemBean: VariableItemBean) {
        viewModelScope.launch {
            val key = variableItemBean.name.get()
            if (key.isEmpty() || variableItemBean.value.get().isEmpty()) {
                return@launch
            }
            if (VariableHelper.checkVariableExists(key)) {
                Log.d(TAG, "添加变量失败，该变量已经存在")
                return@launch
            }
            VariableHelper.saveVariable(
                VariableBean(
                    key,
                    variableItemBean.value.get(),
                    variableItemBean.remarks.get(),
                    variableItemBean.isGlobal.get()
                )
            )
            refresh()
        }
    }

    fun updateVariable(variableItemBean: VariableItemBean) {
        viewModelScope.launch {
            val key = variableItemBean.name.get()
            if (key.isEmpty() || variableItemBean.value.get().isEmpty()) {
                return@launch
            }
            if (!VariableHelper.checkVariableExists(key)) {
                Log.d(TAG, "更新变量失败，该变量不存在")
                return@launch
            }
            VariableHelper.updateVariable(
                VariableBean(
                    key,
                    variableItemBean.value.get(),
                    variableItemBean.remarks.get(),
                    variableItemBean.isGlobal.get()
                )
            )
            refresh()
        }
    }

    fun deleteVariable(variableItemBean: VariableItemBean) {
        viewModelScope.launch {
            val key = variableItemBean.name.get()
            if (key.isEmpty()) {
                return@launch
            }
            VariableHelper.deleteVariable(key)
            refresh()
        }
    }

    private suspend fun refresh() {
        variableRepository.variableBeanFlow.collect {
            variableItemList.value = it.map { variableBean ->
                VariableItemBean.convert(variableBean)
            }
        }
    }

}