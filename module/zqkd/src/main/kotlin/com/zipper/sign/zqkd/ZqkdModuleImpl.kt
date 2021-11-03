package com.zipper.sign.zqkd

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.sign.core.IModuleProvider
import com.zipper.sign.core.base.BaseApi
import kotlinx.coroutines.*
import java.io.InputStream
import java.nio.charset.StandardCharsets

class ZqkdModuleImpl : IModuleProvider {
    @ObsoleteCoroutinesApi
    override suspend fun execute(inputStream: InputStream?): List<Deferred<BaseApi>> =
        withContext(newSingleThreadContext("zqkd")) {
            val configJson = inputStream?.use {
                val reader = it.reader(StandardCharsets.UTF_8)
                reader.readText()
            } ?: "[]"

            val list: List<ZqkdApiParam> = Gson().fromJson(configJson, object : TypeToken<List<ZqkdApiParam>>() {}.type)
            return@withContext list.map {
                async {
                    ZqkdApi().run {
                        execute(it)
                        this
                    }
                }
            }
        }
}