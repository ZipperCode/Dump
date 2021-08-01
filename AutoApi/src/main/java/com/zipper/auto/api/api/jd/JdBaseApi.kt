package com.zipper.auto.api.api.jd

import UserAgents
import android.content.Context
import androidx.annotation.CallSuper
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.utils.MemoryManager
import com.google.gson.Gson
import com.zipper.auto.api.net.HttpHelper
import com.zipper.auto.api.net.cookie.CookieStore
import com.zipper.core.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.*

abstract class JdBaseApi {

    protected open val cookieStore: CookieStore = HttpHelper.cookieStore

    protected open val okHttpClient = HttpHelper.okHttpClient

    protected open val userAgent = UserAgents.agent

    protected open val gson = Gson()

    abstract fun domain(): String

    protected open suspend fun get(url: String, headers: Map<String, String> = emptyMap()): String {
        val requestBuilder = Request.Builder()

        requestBuilder.url(url).get()

        for (header in headers) {
            requestBuilder.addHeader(header.key, header.value)
        }
        return request(requestBuilder.build())
    }

    protected open suspend fun post(
        url: String,
        body: Map<String, Any> = emptyMap(),
        headers: Map<String, String> = emptyMap()
    ): String {
        val formBodyBuilder = FormBody.Builder()

        for (entry in body) {
            formBodyBuilder.addEncoded(entry.key, entry.value.toString())
        }
        return post(url, formBodyBuilder.build(), headers)
    }

    protected open suspend fun post(
        url: String,
        body: RequestBody,
        headers: Map<String, String> = emptyMap()
    ): String {
        val requestBuilder = Request.Builder()

        requestBuilder.url(url).post(body)

        for (header in headers) {
            requestBuilder.addHeader(header.key, header.value)
        }
        return request(requestBuilder.build())
    }

    protected open suspend fun request(request: Request): String = withContext(Dispatchers.IO) {
        val uri = request.url.toUri()
        val httpUrl = HttpUrl.Builder()
            .scheme(uri.scheme)
            .host(uri.host)
            .build()

        if (request.header("Cookie") == null) {
            val cookies = cookieStore.load(httpUrl)
            if (cookies.isEmpty()) {
                cookieStore.save(httpUrl, getRootCookie().map {
                    Cookie.Builder()
                        .name(it.name)
                        .value(it.value)
                        .domain(httpUrl.host)
                        .build()
                }.toList())
            }
        }

        val response = okHttpClient.newCall(request).execute()
        if (response.isSuccessful) {
            return@withContext response.body?.string() ?: "{}"
        }
        L.e(response.toString())
        return@withContext "{}"
    }

    protected open fun getRootCookie(): List<Cookie> {
        return arrayListOf(
            Cookie.Builder()
                .name("pt_key")
                .value("AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0")
                .domain(domain())
                .build(),

            Cookie.Builder()
                .name("pt_pin")
                .value("jd_59739e7a7a296")
                .domain(domain())
                .build()
        )
    }

    protected inline fun<reified T> convert(json: String): T{
        return gson.fromJson(json, T::class.java)
    }

    protected open val cacheScript: MutableMap<String, V8> = mutableMapOf()

    protected open val cacheScriptMemManager:MutableMap<String, MemoryManager> = mutableMapOf()

    @CallSuper
    open suspend fun main(context: Context){
        execute(context)
        delay(5000)
        destroy()
    }

    protected abstract suspend fun execute(context: Context)

    @CallSuper
    open suspend fun destroy(){
        cacheScriptMemManager.forEach{
            it.value.release()
        }
    }

    protected suspend fun runScriptFunc(fileName: String, funcName: String,vararg args: Any): Any?{
       return cacheScript[fileName]?.use {
           val params = V8Array(it)
           for (arg in args){
               params.push(arg)
           }
           it.executeFunction(funcName,params)
        }
    }

    protected suspend fun runScriptMethod(fileName: String, objName: String, funcName: String,vararg args: Any): Any?{
        return cacheScript[fileName]?.use {
            val params = V8Array(it)
            for (arg in args){
                params.push(arg)
            }
            val obj = it.getObject(objName)
            obj.executeFunction(funcName,params)
        }
    }

}