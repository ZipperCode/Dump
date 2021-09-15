package com.zipper.auto.api.activity.bean

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.zipper.api.module.bean.ApiVariableBean

data class VariableItemBean(
    val name: ObservableField<String>,
    val value: ObservableField<String>,
    val remarks: ObservableField<String> = ObservableField(""),
    val isGlobal:ObservableBoolean = ObservableBoolean(false)
) {


    companion object{
        fun convert(apiVariableBean: ApiVariableBean): VariableItemBean{
            return VariableItemBean(
                ObservableField(apiVariableBean.name),
                ObservableField(apiVariableBean.value),
                ObservableField(apiVariableBean.remarks),
                ObservableBoolean(apiVariableBean.isGlobal)
            )
        }
    }

}