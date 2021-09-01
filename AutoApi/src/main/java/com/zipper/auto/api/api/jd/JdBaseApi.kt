package com.zipper.auto.api.api.jd

import UserAgents
import android.content.Context
import androidx.annotation.CallSuper
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.utils.MemoryManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.auto.api.api.BaseApi
import com.zipper.auto.api.net.HttpHelper
import com.zipper.auto.api.net.LoggerInterceptor
import com.zipper.auto.api.net.TrustAllCerts
import com.zipper.auto.api.net.cookie.CookieInterceptor
import com.zipper.auto.api.net.cookie.CookieJarImpl
import com.zipper.auto.api.net.cookie.CookieStore
import com.zipper.auto.api.net.cookie.PersistentCookieStore
import com.zipper.core.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

abstract class JdBaseApi(protected val storeKey: String) {

    protected open val cookieStore: CookieStore = PersistentCookieStore(storeKey)

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
//        .cookieJar(CookieJarImpl(cookieStore))
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
        .sslSocketFactory(BaseApi.createSSLSocketFactory(), TrustAllCerts())
        .addInterceptor(LoggerInterceptor())
        .addInterceptor(CookieInterceptor(storeKey))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    protected open val userAgent = UserAgents.agent

    val gson = Gson()

    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(domain())
        .client(okHttpClient)
        .build()

    protected open var isLogin = false

    abstract fun domain(): String

    protected fun catchExp(block: () -> Unit){
        try {
            block.invoke()
        }catch (e: Throwable){
            e.printStackTrace()
        }
    }

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

    protected inline fun <reified T> convert(json: String): T {
        return gson.fromJson(json, T::class.java)
    }

    protected fun convertMap(json: String): Map<String, Any> {
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }

    protected open val cacheScript: MutableMap<String, V8> = mutableMapOf()

    protected open val cacheScriptMemManager: MutableMap<String, MemoryManager> = mutableMapOf()

    @CallSuper
    open suspend fun main(context: Context) {
        execute(context)
        delay(5000)
        destroy()
    }

    protected abstract suspend fun execute(context: Context)

    @CallSuper
    open suspend fun destroy() {
        cacheScriptMemManager.forEach {
            it.value.release()
        }
    }

    protected suspend fun runScriptFunc(
        fileName: String,
        funcName: String,
        vararg args: Any
    ): Any? {
        return cacheScript[fileName]?.use {
            val params = V8Array(it)
            for (arg in args) {
                params.push(arg)
            }
            it.executeFunction(funcName, params)
        }
    }

    protected suspend fun runScriptMethod(
        fileName: String,
        objName: String,
        funcName: String,
        vararg args: Any
    ): Any? {
        return cacheScript[fileName]?.use {
            val params = V8Array(it)
            for (arg in args) {
                params.push(arg)
            }
            val obj = it.getObject(objName)
            obj.executeFunction(funcName, params)
        }
    }

}