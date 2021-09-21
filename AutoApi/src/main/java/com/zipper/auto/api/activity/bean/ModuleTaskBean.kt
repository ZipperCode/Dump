package com.zipper.auto.api.activity.bean

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.zipper.api.module.bean.ApiModuleInfo

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
        fun convert(apiModuleInfo: ApiModuleInfo): ModuleTaskBean{
            return ModuleTaskBean(
                apiModuleInfo.moduleKey,
                ObservableField(apiModuleInfo.moduleName),
                ObservableField(apiModuleInfo.getExecuteTimeFormat()),
                moduleRunStatus = ObservableBoolean(apiModuleInfo.isRun)
            )
        }
    }
}
