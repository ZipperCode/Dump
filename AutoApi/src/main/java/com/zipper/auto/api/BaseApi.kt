package com.zipper.auto.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
abstract class BaseApi {

    companion object {
        val DEFAULT_HEADER = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36"
        )

        @JvmStatic
        @Throws(Exception::class)
        fun createSSLSocketFactory(): SSLSocketFactory {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            return sc.socketFactory
        }
    }

    protected val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
        .sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
        .addNetworkInterceptor(LoggerInterceptor())
        .build()

    protected fun customHeader(): Map<String, String> = emptyMap()

    protected suspend fun get(url: String, headers: Map<String, String> = DEFAULT_HEADER): String{
        return request(url, headers = headers)
    }

    protected suspend fun post(url: String, params: Map<String, String> = emptyMap(), headers: Map<String, String> = DEFAULT_HEADER): String{
        return request(url, HttpMethod.POST, params, headers)
    }

    protected suspend fun request(
        url: String,
        method: HttpMethod = HttpMethod.GET,
        params: Map<String, String> = emptyMap(),
        headers: Map<String, String> = DEFAULT_HEADER
    ): String {
        val requestBuilder = Request.Builder()
            .url(url)

        if (method == HttpMethod.POST) {
            val formBodyBuilder = FormBody.Builder()
            for (param in params) {
                formBodyBuilder.addEncoded(param.key, param.value)
            }
            requestBuilder.post(formBodyBuilder.build())
        } else {
            requestBuilder.get()
        }

        val customHeader = customHeader()
        for (header in customHeader) {
            requestBuilder.addHeader(header.key, header.value)
        }

        for (header in headers) {
            requestBuilder.addHeader(header.key, header.value)
        }

        return request(requestBuilder.build())
    }

    protected suspend fun request(request: Request): String = withContext(Dispatchers.IO) {
        val response = okHttpClient.newCall(request).execute()
        try {
            return@withContext response.body?.string() ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}