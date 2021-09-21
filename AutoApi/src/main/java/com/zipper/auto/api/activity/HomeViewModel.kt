package com.zipper.auto.api.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.api.module.ApiModuleManager
import com.zipper.auto.api.activity.bean.ModuleTaskBean
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    companion object{
        const val TAG: String = "HomeViewModel"
    }

    private val _taskInfoList: MutableLiveData<MutableList<ModuleTaskBean>> = MutableLiveData()

    val taskInfoList: LiveData<MutableList<ModuleTaskBean>> get() = _taskInfoList

    private val variableRepository = VariableRepository()

    init {
        viewModelScope.launch {
            _taskInfoList.value = ApiModuleManager.moduleInfoList.map { apiModuleInfo ->
                ModuleTaskBean.convert(apiModuleInfo)
            }.toMutableList()
        }
    }

    fun customCallModule(moduleKey: String){
        viewModelScope.launch {
            variableRepository.apiVariableBeanFlow.collect{
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
    fun banModule(bean: ModuleTaskBean){
        if (bean.moduleIsBan.get()){
            if(ApiModuleManager.resumeModule(bean.moduleKey)){
                bean.moduleIsBan.set(false)
                // TODO 获取调度情况
                bean.moduleRunStatus.set(false)
            }
        }else{
            if(ApiModuleManager.banModule(bean.moduleKey)){
                bean.moduleIsBan.set(true)
                bean.moduleRunStatus.set(false)
            }
        }
    }

    fun stopModule(bean: ModuleTaskBean){
        if (bean.isRun){
            bean.moduleRunStatus.set(ApiModuleManager.stopModule(bean.moduleKey))
        }
    }

    /**
     * 删除模块
     */
    fun delModule(bean: ModuleTaskBean){
        if (ApiModuleManager.removeModuleByKey(bean.moduleKey)){
            _taskInfoList.value?.remove(bean)
            _taskInfoList.value = _taskInfoList.value
        }
    }

}