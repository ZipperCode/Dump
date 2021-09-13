package com.zipper.auto.api.activity.bean

import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong

data class ModuleTaskBean(
    val moduleKey: String,
    val moduleName: ObservableField<String>,
    val moduleExecTime: ObservableLong
){

    val moduleExecTimeFormat: String get() = ""
}
