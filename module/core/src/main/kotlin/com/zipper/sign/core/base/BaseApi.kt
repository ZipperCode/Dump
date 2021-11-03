package com.zipper.sign.core.base

import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.jvm.Throws

abstract class BaseApi(val domain: String) {

    protected open val okHttpClient = getOkHttp()

    protected open val gson = Gson()

    val retrofit: Retrofit = createRetrofit()

    private fun getOkHttp(): OkHttpClient{
        val interceptors = getInterceptors()
        val networkInterceptors = getNetworkInterceptors()
       val builder =  OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
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

    private fun createRetrofit(): Retrofit{
        val builder =  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(domain)
            .client(okHttpClient)

        val converterFactory = getConvertFactory()

        converterFactory.forEach {
            builder.addConverterFactory(it)
        }

        return builder.build()
    }

    protected inline fun<reified T> create(): T{
        return retrofit.create(T::class.java)
    }

    protected open fun getInterceptors(): List<Interceptor> = emptyList()

    protected open fun getNetworkInterceptors(): List<Interceptor> = emptyList()

    protected open fun getConvertFactory():List<Converter.Factory> = emptyList()

    open suspend fun execute(){}

    open suspend fun testExecute(){}

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

        class TrustAllCerts: X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

        }
    }
}