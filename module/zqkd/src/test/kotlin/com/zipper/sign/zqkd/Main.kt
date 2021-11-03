package com.zipper.sign.zqkd

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.nio.charset.StandardCharsets


@ObsoleteCoroutinesApi
fun main() = runBlocking(newSingleThreadContext("zqkd")) {
    val configJson = Thread.currentThread().contextClassLoader
        .getResourceAsStream("zqkd_config.json")?.use {
        val reader = it.reader(StandardCharsets.UTF_8)
        reader.readText()
    }

    val list: List<ZqkdApiParam> = Gson().fromJson(configJson, object : TypeToken<List<ZqkdApiParam>>() {}.type)
    list.map {
        async { ZqkdApi().execute(it) }
    }.forEach {
        it.await()
    }

//    ZqkdApi().execute(
//        ZqkdApiParam(
//            "59370586",
//            "6cc8d0db8c0e8529c1f38d5e7d6cf631",
//            "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdhbOEfHaZr7m6apqGcXY",
//            "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdgW2FsnbgrqnMapqGcXY",
//            "嘿！哪誰"
//        )
//    )
//    ZqkdApi().execute(
//        ZqkdApiParam(
//            "59529362",
//            "c1e18505a272af3188ae4eb4da9a4b2f",
//            "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFuauyt4FrhKKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdhWyEjKDdr9-6apqGcXY",
//            "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFuauyt4FrhKKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdhWyEjKDdr9-6apqGcXY",
//            "0"
//        )
//    )
}

suspend fun test1(){
        ZqkdApi().execute(
            ZqkdApiParam(
                "59370586",
                "6cc8d0db8c0e8529c1f38d5e7d6cf631",
                "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdhbOEfHaZr7m6apqGcXY",
                "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdgW2FsnbgrqnMapqGcXY",
                "嘿！哪誰"
            )
    )
}