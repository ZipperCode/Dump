package com.zipper.auto.api.script

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.auto.api.api.BaseApi
import com.zipper.auto.api.net.LoggerInterceptor
import com.zipper.auto.api.net.TrustAllCerts
import com.zipper.core.utils.L
import com.zipper.core.utils.StringUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

object HttpHelper {

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
        .sslSocketFactory(BaseApi.createSSLSocketFactory(), TrustAllCerts())
        .addInterceptor(LoggerInterceptor())
        .build()

    private val gson = Gson()

    fun get(jsRequestBean: JsRequestBean, callback: (HttpResponseBean) -> Unit) {
        val requestBuilder = Request.Builder()

        requestBuilder.url(jsRequestBean.url).get()

        for (header in jsRequestBean.headers) {
            requestBuilder.addHeader(header.key, header.value)
        }
        request(requestBuilder.build(), callback)
    }

    fun post(jsRequestBean: JsRequestBean, callback: (HttpResponseBean) -> Unit) {
        val requestBuilder = Request.Builder()

        requestBuilder.url(jsRequestBean.url)

        if(jsRequestBean.body.isNotEmpty()){
            try{
                val formBodyBuilder = FormBody.Builder()
                val bodyMap = convertMap(jsRequestBean.body)
                for (entry in bodyMap) {
                    formBodyBuilder.addEncoded(entry.key, entry.value.toString())
                }
                requestBuilder.post(formBodyBuilder.build())
            }catch (e: Exception){
                requestBuilder.post(RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    jsRequestBean.body
                ))
            }
        }

        for (header in jsRequestBean.headers) {
            requestBuilder.addHeader(header.key, header.value)
        }

        request(requestBuilder.build(), callback)
    }

    private fun request(request: Request, callback: (HttpResponseBean) -> Unit) {
        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                callback(HttpResponseBean(response.code, response.body?.string() ?: "{}"))
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(HttpResponseBean(-1, e.message.toString()))
            }
        })
    }

    fun convertMap(json: String): Map<String, Any> {
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }

}