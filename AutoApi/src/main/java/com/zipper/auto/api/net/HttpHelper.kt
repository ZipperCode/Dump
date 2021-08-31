package com.zipper.auto.api.net

import com.zipper.auto.api.api.BaseApi
import com.zipper.auto.api.net.cookie.CookieJarImpl
import com.zipper.auto.api.net.cookie.CookieStore
import com.zipper.auto.api.net.cookie.PersistentCookieStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

object HttpHelper {

    val cookieStore: CookieStore = PersistentCookieStore("")

    val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .cookieJar(CookieJarImpl(cookieStore))
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
        .sslSocketFactory(BaseApi.createSSLSocketFactory(), TrustAllCerts())
        .addInterceptor(LoggerInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()
}