package com.zipper.auto.api.api.jd.user

import com.zipper.auto.api.api.jd.user.bean.JdGetTokenResp
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface JdUserService {
    /**
     * 获取jd token
     * url参数和header可自行更换
     * cookie: eg: pin=;wskey=;
     */
    @FormUrlEncoded
    @POST("client.action?functionId=genToken&clientVersion=10.1.2&build=89743&client=android&d_brand=Xiaomi&d_model=MI6&osVersion=11&screen=1920*1080&partner=cymqj01&oaid=&openudid=33baee855d0b0f01&eid=eidA21868122e4s7N6plxAVTTa2rBpLO5czqgjeqZz+pWAzhkJ+in6Q2YJglj12RgiEtYXlox+seREKxgrmXHH9B2ULF8ww2xNhzPeoZ2MvWtE08r6I0&sdkVersion=30&lang=zh_CN&uuid=33baee855d0b0f01&aid=33baee855d0b0f01&networkType=wifi&wifiBssid=unknown&uemps=0-2&harmonyOs=0&st=1630546768163&sign=e9c4ad8fdf9a172f04d4c6976d7e20a0&sv=112")
    @Headers("user-agent:okhttp/3.12.1;jdmall;android;version/10.1.2;build/89743;screen/1080x1920;os/11;network/wifi;",
        "Content-Type:application/x-www-form-urlencoded; charset=UTF-8"
    )
    suspend fun getToken(@Field("body") body: String = """{"to":"https%3a%2f%2fplogin.m.jd.com%2fjd-mlogin%2fstatic%2fhtml%2fappjmp_blank.html"}"""): JdGetTokenResp


}