package com.zipper.task.module.bean

data class VariableBean(
    val name: String,
    val value: String,
    val remarks: String,
    val isGlobal: Boolean,
    val isBan: Boolean = false
){
    fun check(): Boolean{
        return name.isNotEmpty() && value.isNotEmpty()
    }
}
