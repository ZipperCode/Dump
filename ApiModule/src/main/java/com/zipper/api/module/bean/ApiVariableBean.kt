package com.zipper.api.module.bean

data class ApiVariableBean(
    val name: String,
    val value: String,
    val remarks: String,
    val isGlobal: Boolean,
    val isBan: Boolean,
    val usedModule:List<String>
)
