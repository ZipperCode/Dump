package com.zipper.auto.api.activity.bean

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class VariableBean(
    val name: ObservableField<String>,
    val value: ObservableField<String>,
    val remarks: ObservableField<String> = ObservableField(""),
    val isGlobal:ObservableBoolean = ObservableBoolean(false)
) {


}