package com.zipper.sign.core.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class NewInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(newRequest(request))
    }

    abstract fun newRequest(oldRequest: Request): Request
}