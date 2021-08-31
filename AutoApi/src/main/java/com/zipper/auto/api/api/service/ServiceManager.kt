package com.zipper.auto.api.api.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceManager {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    val gson: Gson = Gson()


    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://api.m.jd.com")
        .client(httpClient)
        .build()



    fun getService(): JdNecklaceService{
        return retrofit.create(JdNecklaceService::class.java)
    }

}
