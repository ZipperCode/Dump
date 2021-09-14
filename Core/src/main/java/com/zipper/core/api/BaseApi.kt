package com.zipper.core.api

import android.annotation.SuppressLint
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

abstract class BaseApi(protected val domain: String) {

    protected val okHttpClient = getOkHttp()
    val gson = Gson()

    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(domain)
        .client(okHttpClient)
        .build()

    private fun getOkHttp(): OkHttpClient{
        val interceptors = getInterceptors()
        val networkInterceptors = getNetworkInterceptors()
       val builder =  OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
        interceptors.forEach{
            builder.addInterceptor(it)
        }
        networkInterceptors.forEach {
            builder.addNetworkInterceptor(it)
        }

        return builder.build()
    }

    protected inline fun<reified T> create(): T{
        return retrofit.create(T::class.java)
    }

    protected open fun getInterceptors(): List<Interceptor> = emptyList()

    protected open fun getNetworkInterceptors(): List<Interceptor> = emptyList()

    abstract suspend fun execute()

    suspend fun<T> catchExp(block: suspend () -> T?): T?{
        return try {
            block.invoke()
        }catch (e: Throwable){
            e.printStackTrace()
            null
        }
    }


    companion object{
        @JvmStatic
        @Throws(Exception::class)
        fun createSSLSocketFactory(): SSLSocketFactory {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            return sc.socketFactory
        }

        @SuppressLint("CustomX509TrustManager")
        class TrustAllCerts: X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

        }
    }
}