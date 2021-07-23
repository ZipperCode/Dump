package com.zipper.auto.api

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.StringBuilder

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
class LoggerInterceptor : Interceptor {

    companion object {
        const val TAG: String = "LoggerInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        log("[---------------------------------------------------------------]")
        log("[Method] = ${request.method}")
        log("[Url] = ${request.url}")
        val urlEncodeParam = parseUrlEncodeParam(request.url.query)
        log("[UrlEncodeParam] = $urlEncodeParam")
        val requestBody = request.body
        if (requestBody != null) {
            when (requestBody) {
                is FormBody -> {
                    val builder = StringBuilder(requestBody.contentLength().toInt())
                    for (index in 0..requestBody.size) {
                        builder.append("\t")
                            .append(requestBody.name(index)).append("=").append(requestBody.value(index))
                            .append("\t Encode[ ")
                            .append(requestBody.encodedName(index)).append("=").append(requestBody.encodedValue(index))
                            .append(" ]\n")
                    }
                    log("[FormParam] = ${builder.toString()}")
                }
            }
        }

        log("[Header] = ")
        for (header in request.headers) {
            log("\t ${header.first} = ${header.second}")
        }
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()

        log("[Time] = ${endTime - startTime}")
        val contentType = response.body?.contentType()
        val contentLength = response.body?.contentLength() ?: -1
        val content = response.body?.string() ?: ""
        log("[Content-Type] = $contentType")
        log("[Content-contentLength] = $contentLength")
        log("[Content] = $content")
        log("[---------------------------------------------------------------]")
        return response.newBuilder()
            .body(ResponseBody.Companion.create(contentType, content))
            .build()
    }

    private fun parseUrlEncodeParam(query: String?): Map<String, String>{
        val params = mutableMapOf<String, String>()
        if(query == null){
            return params
        }
        val paramSplits = query.split("&")
        for (param in paramSplits){
            if(param.contains("=")){
                val paramDetail = param.split("=")
                if(paramDetail.size == 2){
                    params[paramDetail[0]] = paramDetail[1]
                }else{
                    params[paramDetail[0]] = ""
                }
            }
        }
        return params
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }
}