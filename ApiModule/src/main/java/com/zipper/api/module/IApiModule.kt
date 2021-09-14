package com.zipper.api.module

import androidx.work.WorkRequest

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
interface IApiModule {

    fun moduleUniqueKey(): String

    fun debugMode(debug: Boolean)

    /**
     * 注入环境变量
     */
    fun setVariable(variableMap: Map<String, String>)

    /**
     * 执行任务
     */
    fun execute()
}