package com.zipper.auto.api.activity

import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.api.module.ApiModuleManager
import com.zipper.auto.api.activity.bean.ModuleTaskBean
import com.zipper.auto.api.activity.bean.TaskInfoBean
import com.zipper.core.utils.L
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collectors

class HomeViewModel: ViewModel() {

    companion object{
        const val TAG: String = "HomeViewModel"
    }

    private val _taskInfoList: MutableLiveData<List<ModuleTaskBean>> = MutableLiveData()

    val taskInfoList: LiveData<List<ModuleTaskBean>> get() = _taskInfoList

    private val variableRepository = VariableRepository()

    init {
        viewModelScope.launch {
            _taskInfoList.value = ApiModuleManager.moduleInfoList.map { apiModuleInfo ->
                ModuleTaskBean.convert(apiModuleInfo)
            }
        }
    }

    fun customCallModule(moduleKey: String){
        viewModelScope.launch {
            variableRepository.apiModuleListFlow.collect{
                val canUsedVariableMap = it.filter { variable ->
                    variable.isGlobal || variable.usedModule.contains(moduleKey)
                }.associate { variable ->
                    variable.name to variable.value
                }

                ApiModuleManager.callModuleMain(moduleKey, canUsedVariableMap)

            }
        }
    }

    /**
     * 禁用模块
     */
    fun banModule(key: String){

    }

    /**
     * 删除模块
     */
    fun delModule(keu: String){

    }

}