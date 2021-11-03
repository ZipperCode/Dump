package com.zipper.sign.core

import kotlinx.coroutines.delay
import okhttp3.Call
import okhttp3.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.floor

val millsTime: Long get() = Date().time

val secondTime: Long get() = Date().time / 1000

val millsTimeStr: String get() = "$millsTime"

val secondTimeStr: String get() = "$secondTime"

fun Map<String, Any>.toUrlParam(): String{
    val builder = StringBuilder()
    forEach { k, v ->
        builder.append(k).append("=").append(v).append("&")
    }
    return builder.deleteCharAt(builder.length - 1).toString()
}

fun randomInt(min: Int = 0, max: Int = 100): Int {
    return kotlin.math.min(floor(min + Math.random() * (max - min)).toInt(), max)
}

suspend fun delaySecond(timeSecond: Int) {
    delay(timeSecond * 1000L)
}

suspend fun delayRandomSecond(timeSecondStart: Int = 3, timeSecondEnd: Int = 6) {
    val randomBoolean = Random().nextBoolean()
    if (randomBoolean){
        delaySecond((timeSecondStart + (Math.random() * (timeSecondEnd - timeSecondStart)).toInt()))
    }else{
        delaySecond((timeSecondStart + 1 - (Math.random() * (timeSecondEnd - timeSecondStart)).toInt()))
    }
}

suspend fun<T> catchException(block: suspend () -> T?): T?{
    return try {
        block.invoke()
    }catch (e: Throwable){
        e.printStackTrace()
        null
    }
}

suspend fun<T> Response<T>.result(success: suspend (T) -> Unit){
    try {
        val bodyResult = body()
        if (isSuccessful){
            if (bodyResult == null){
                throw Exception("${raw().request.url} ==> responseBody == null")
            }else{
                success.invoke(bodyResult)
            }
        }else{
            throw Exception("${raw().request.url} ==> http code = ${code()} msg = ${message()}")
        }
    }catch (e: Exception){
        e.printStackTrace()
        L.e("${e.message}")
    }
}


suspend fun Call.awaitResponse(): okhttp3.Response {

    return suspendCoroutine {
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    it.resume(response)
                }catch (e: Exception){
                    e.printStackTrace()
                    it.resumeWithException(e)
                }
            }
        })
    }
}


