package com.zipper.task.module

/**
 * @param moduleKey 模块名
 * @param moduleVersion 模块版本
 * @param modulePath 模块路径
 * @param isLocal 是否本地模块
 * @param isBan 模块是否禁用
 */
data class TaskModuleInfo(
    val moduleKey:String,
    val moduleName: String,
    val moduleImpl:String,
    val moduleVersion: Int = 1,
    val modulePath: String = "",
    val isLocal: Boolean = false,
    val isBan: Boolean = false
)
