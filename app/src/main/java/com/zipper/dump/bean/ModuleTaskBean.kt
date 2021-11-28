package com.zipper.dump.bean


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.zipper.task.module.TaskModuleInfo

/**
 * @param moduleKey
 * @param moduleName
 * @param moduleExecTime
 * @param moduleIsBan
 * @param moduleRunStatus
 * @param moduleVersion
 */
data class ModuleTaskBean(
    val moduleKey: String,
    val moduleName: ObservableField<String>,
    val moduleExecTime: ObservableField<String>,
    val moduleIsBan: ObservableBoolean = ObservableBoolean(true),
    val moduleRunStatus: ObservableBoolean = ObservableBoolean(false),
    val moduleVersion: ObservableInt = ObservableInt(100)
){

    val isRun: Boolean get() = !moduleIsBan.get() && moduleRunStatus.get()

    val moduleVersionFormat: String get() {
        val bugVersion = moduleVersion.get() % 100
        val version = moduleVersion.get() / 100
        return "v$version.$bugVersion"
    }

    companion object{
        fun convert(taskModuleInfo: TaskModuleInfo, isRun: Boolean = false): ModuleTaskBean{
            return ModuleTaskBean(
                taskModuleInfo.moduleKey,
                ObservableField(taskModuleInfo.moduleName),
                ObservableField(taskModuleInfo.moduleVersion.toString()),
                ObservableBoolean(false)
            )
        }
    }
}
