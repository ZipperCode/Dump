package com.zipper.auto.api.activity.bean

import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import com.zipper.api.module.bean.ApiModuleInfo

data class ModuleTaskBean(
    val moduleKey: String,
    val moduleName: ObservableField<String>,
    val moduleExecTime: ObservableLong
){

    val moduleExecTimeFormat: String get() = ""

    companion object{
        fun convert(apiModuleInfo: ApiModuleInfo): ModuleTaskBean{
            return ModuleTaskBean(apiModuleInfo.moduleKey, ObservableField(apiModuleInfo.moduleName), ObservableLong(0L))
        }
    }
}
