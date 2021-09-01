package com.zipper.auto.api.net.cookie

import com.zipper.core.utils.SpUtil
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor(val cookieStoreKey: String): Interceptor {

    companion object{
        const val COOKIE_STORE_SP_NAME = "sp_jd_cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val host = originalRequest.url.host

        if(!host.contains(".jd.com")){
            return chain.proceed(originalRequest)
        }

        val cookie = SpUtil.instance(COOKIE_STORE_SP_NAME).get(cookieStoreKey, "")

        val newRequest = originalRequest.newBuilder()
            .header("Cookie",cookie)
            .build()
        return chain.proceed(newRequest)
    }
}