package com.zipper.testmodule

import androidx.fragment.app.Fragment
import com.zipper.api.module.ApiModuleContext
import com.zipper.api.module.BaseApiModule
import com.zipper.api.module.MLog

class TestModuleImpl(context: ApiModuleContext): BaseApiModule(context) {

    companion object{
        const val TAG: String = "TextModuleImpl"
        @JvmStatic
        val instance get() = instance<TestModuleImpl>()
    }

    override fun moduleUniqueKey(): String = "text_module_key"
    override fun getMainFragment(): Fragment {
        return TestFragment()
    }

    override fun getFragmentClass(): List<Class<Fragment>> {
        TODO("Not yet implemented")
    }

    override fun setVariable(variableMap: Map<String, String>) {
        MLog.d(TAG,"注入环境变量 ==> $variableMap")
    }

    override fun execute() {
        MLog.d(TAG,"test >> 执行TextModule模块")
    }

    override fun release() {
        MLog.d(TAG,"test >> release ")
    }
}