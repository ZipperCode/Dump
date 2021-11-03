package com.zipper.sign.kugou

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.sign.core.ApiException
import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
fun main() = runBlocking(newSingleThreadContext("kg")) {
    val json = Thread.currentThread().contextClassLoader
        .getResourceAsStream("kg_config.json")?.use {
            it.reader().readText()
        }?: "[]"

    val userList: List<ConfigBean> = Gson().fromJson(json, object : TypeToken<List<ConfigBean>>(){}.type)
    userList.map {
        async {
            KgTaskApi(it).execute()
        }
    }.forEach {
        it.await()
    }
}

fun test1(){
    println(KgTaskApi.signature(mapOf(
        "appid" to "1005",
        "clienttime" to "1634917564609",
        "clientver" to "10829",
        "dfid" to "4Wwvsj2nDISI017CmR4L886I",
        "from" to "client",
        "mid" to "19866310614991365975980225640804152227",
        "srcappid" to "2919",
        "token" to "61c0488e45e44e421ec0aa0210b34e20c4b5d57d4be051b788d3faa07b7f8ca2",
        "userid" to "128004709",
        "uuid" to "532544371bb026cc684489e57eedbe9c"
    ),"""{"code":"20211022"}"""))
}