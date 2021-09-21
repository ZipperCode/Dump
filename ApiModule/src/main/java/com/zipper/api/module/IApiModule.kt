package com.zipper.api.module

import android.content.Context
import androidx.fragment.app.Fragment

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
interface IApiModule {

    fun moduleUniqueKey(): String

    fun debugMode(debug: Boolean)

    fun getModuleContext(): Context

    fun getMainFragment(): Fragment?

    fun getFragmentClass(): List<Class<Fragment>>

    /**
     * 注入环境变量
     */
    fun setVariable(variableMap: Map<String, String>)

    /**
     * 执行任务
     */
    fun execute()

    fun stop()

    fun release();
}