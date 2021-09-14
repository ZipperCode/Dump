package com.zipper.api.kgmodule

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
object ModuleHelper {

    val spKey = ""

    val variableKey = ""

    suspend fun run(userId: String, token: String){
        val kgTaskApi = KgTaskApi()
        kgTaskApi.run {
            init(userId, token)
            execute()
        }

    }
}