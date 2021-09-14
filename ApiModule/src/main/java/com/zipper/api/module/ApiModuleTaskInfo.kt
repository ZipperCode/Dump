package com.zipper.api.module

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
data class ApiModuleTaskInfo(
    val taskKey:String,
    val taskName: String,
    val description: String,
    val execTime: Long
)