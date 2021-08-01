package com.zipper.auto.api.api.service

import retrofit2.http.*

interface JdNecklaceService {

    @Headers(
        "accept: application/json, text/plain, */*",
        "content-type: application/x-www-form-urlencoded",
        "origin: https://h5.m.jd.com",
        "accept-language: zh-cn",
        "referer: https://h5.m.jd.com/"
    )
    @POST("/api")
    suspend fun necklaceHome(@QueryMap param: Map<String,*>, @Body body: String, @HeaderMap headers: Map<String,String>)
}