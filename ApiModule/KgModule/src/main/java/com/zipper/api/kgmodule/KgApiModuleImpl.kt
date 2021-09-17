package com.zipper.api.kgmodule

import android.content.Context
import androidx.fragment.app.Fragment
import com.zipper.api.module.ApiModuleContext
import com.zipper.api.module.BaseApiModule
import com.zipper.api.module.MLog
/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
class KgApiModuleImpl(context: ApiModuleContext): BaseApiModule(context) {

    companion object{
        const val TAG: String = "KgApiModuleImpl"
    }

    override fun moduleUniqueKey(): String = "KG_MODULE_UNIQUE_KEY"
    override fun getMainFragment(): Fragment? {
        return null
    }

    private val variableMap = mutableMapOf<String,String>()

    override fun getModuleContext(): Context {
        return context
    }

    override fun setVariable(variableMap: Map<String, String>) {
        MLog.d(TAG,"注入环境变量 ==> $variableMap")
        this.variableMap.clear()
        this.variableMap.putAll(variableMap)
    }

    override fun execute() {
        val userId = variableMap["KG_MODULE_USER_ID"]
        val token = variableMap["KG_MODULE_TOKEN"]
        val h5Token = variableMap["KG_MODULE_H5_TOKEN"]
    }
}