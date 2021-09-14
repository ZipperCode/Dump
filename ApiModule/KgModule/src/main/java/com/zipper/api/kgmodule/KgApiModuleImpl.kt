package com.zipper.api.kgmodule

import com.zipper.api.module.IApiModule
import com.zipper.core.api.BaseApi
import com.zipper.core.utils.L

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
class KgApiModuleImpl: IApiModule {

    companion object{
        const val TAG: String = "KgApiModuleImpl"
    }

    private var debug: Boolean = true

    override fun moduleUniqueKey(): String = "KG_MODULE_UNIQUE_KEY"

    private val variableMap = mutableMapOf<String,String>()

    override fun debugMode(debug: Boolean) {
        this.debug = debug
    }

    override fun setVariable(variableMap: Map<String, String>) {
        L.d(TAG,"注入环境变量 ==> $variableMap")
        this.variableMap.clear()
        this.variableMap.putAll(variableMap)
    }

    override fun execute() {
        val userId = variableMap["KG_MODULE_USER_ID"]
        val token = variableMap["KG_MODULE_TOKEN"]
        val h5Token = variableMap["KG_MODULE_H5_TOKEN"]
    }
}