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
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collectors

class HomeViewModel: ViewModel() {

    companion object{
        const val TAG: String = "HomeViewModel"
    }

    private val _taskInfoList: MutableLiveData<List<ModuleTaskBean>> = MutableLiveData()

    val taskInfoList: LiveData<List<ModuleTaskBean>> get() = _taskInfoList

    private val homeRepository = HomeRepository()

    private val variableRepository = VariableRepository()

    init {

        _taskInfoList.value = listOf(
            ModuleTaskBean("0", ObservableField("123"), ObservableLong(0L)),
            ModuleTaskBean("1", ObservableField("123546"), ObservableLong(0L)),
            ModuleTaskBean("2", ObservableField("234"), ObservableLong(0L)),
            ModuleTaskBean("3", ObservableField("fasdfa"), ObservableLong(0L)),
            ModuleTaskBean("4", ObservableField("fadsf"), ObservableLong(0L))
        )


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