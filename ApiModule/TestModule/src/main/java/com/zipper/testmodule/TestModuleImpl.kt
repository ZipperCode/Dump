package com.zipper.testmodule

import com.zipper.api.module.ApiModuleContext
import com.zipper.api.module.BaseApiModule
import com.zipper.api.module.MLog

class TestModuleImpl(context: ApiModuleContext): BaseApiModule(context) {

    companion object{
        const val TAG: String = "TextModuleImpl"
    }

    override fun moduleUniqueKey(): String = "text_module_key"

    override fun setVariable(variableMap: Map<String, String>) {
        MLog.d(TAG,"注入环境变量 ==> $variableMap")
    }

    override fun execute() {
        MLog.d(TAG,"test >> 执行TextModule模块")
    }
}