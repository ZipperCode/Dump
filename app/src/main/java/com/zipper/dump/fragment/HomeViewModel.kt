package com.zipper.dump.fragment

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.dump.bean.ModuleTaskBean
import com.zipper.task.module.TaskModuleInfo
import com.zipper.task.module.TaskModuleManager
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    companion object {
        const val TAG: String = "HomeViewModel"
    }

    val taskInfoList: MutableLiveData<MutableList<ModuleTaskBean>> = MutableLiveData()

    init {
        taskInfoList.value = TaskModuleManager.taskInfoMap.map {
            val bean = it.value
            ModuleTaskBean.convert(bean, TaskModuleManager.taskIsRun(bean.moduleKey))
        }.toMutableList()
    }

    fun runTask(moduleTaskBean: ModuleTaskBean) {
        viewModelScope.launch {
            if (!TaskModuleManager.taskIsRun(moduleTaskBean.moduleKey)) {
                val result = TaskModuleManager.runTask(moduleTaskBean.moduleKey)
                moduleTaskBean.moduleRunStatus.set(result.isSuccess)
            }
        }
    }

    fun banOrResumeTask(moduleTaskBean: ModuleTaskBean) {
        viewModelScope.launch {
            val isBan = moduleTaskBean.moduleIsBan.get()
            if (isBan) {
                TaskModuleManager.banOrResumeTask(moduleTaskBean.moduleKey, !isBan)
                return@launch
            }

            if (TaskModuleManager.taskIsRun(moduleTaskBean.moduleKey)) {
                val result = TaskModuleManager.stopTask(moduleTaskBean.moduleKey)
                moduleTaskBean.moduleRunStatus.set(result.isSuccess)
                if (result.isFailure) {
                    return@launch
                }
                Log.d(TAG, "成功停止任务${moduleTaskBean.moduleKey}，开始禁用当前模块")
            }
            TaskModuleManager.banOrResumeTask(
                moduleTaskBean.moduleKey,
                moduleTaskBean.moduleIsBan.get()
            )
        }
    }

    fun deleteTask(moduleTaskBean: ModuleTaskBean) {
        viewModelScope.launch {
            if (TaskModuleManager.taskIsRun(moduleTaskBean.moduleKey)) {
                val result = TaskModuleManager.stopTask(moduleTaskBean.moduleKey)
                moduleTaskBean.moduleRunStatus.set(result.isSuccess)
                if (result.isFailure) {
                    Log.d(TAG, "成功停止任务${moduleTaskBean.moduleKey}，开始禁用当前模块")
                    TaskModuleManager.delTask(moduleTaskBean.moduleKey)
                }
            }
        }
    }

}