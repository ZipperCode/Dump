package com.zipper.dump.bean


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.zipper.core.model.binding.ObservableString
import com.zipper.task.module.bean.VariableBean

data class VariableItemBean(
    val name: ObservableString,
    val value: ObservableString,
    val remarks: ObservableString = ObservableString(),
    val isGlobal:ObservableBoolean = ObservableBoolean(false),
    val isShowLargeView: ObservableBoolean = ObservableBoolean(false)
) {

    fun toggleView(){
        val isShow = isShowLargeView.get()
        isShowLargeView.set(!isShow)
    }

    companion object{
        fun convert(variableBean: VariableBean): VariableItemBean{
            return VariableItemBean(
                ObservableString(variableBean.name),
                ObservableString(variableBean.value),
                ObservableString(variableBean.remarks),
                ObservableBoolean(variableBean.isGlobal)
            )
        }
    }

}