package com.zipper.sign.kugou

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.sign.core.ApiException
import com.zipper.sign.core.IModuleProvider
import com.zipper.sign.core.base.BaseApi
import kotlinx.coroutines.*
import java.io.InputStream

class KugouModuleImpl: IModuleProvider {
    @ObsoleteCoroutinesApi
    override suspend fun execute(inputStream: InputStream?): List<Deferred<BaseApi>> = withContext(
        newSingleThreadContext("kg")
    ){
        val json = inputStream?.reader()?.readText() ?: "[]"
        val userList: List<ConfigBean> = Gson().fromJson(json, object : TypeToken<List<ConfigBean>>(){}.type)
        userList.map {
            async {
                KgTaskApi(it).run {
                    execute()
                    this
                }
            }
        }.toList()
    }

}