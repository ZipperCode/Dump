package com.zipper.auto.api.api.jd.delay

import com.zipper.auto.api.api.jd.delay.bean.*
import retrofit2.Call
import retrofit2.http.*

interface JdDelaySignService {

    @FormUrlEncoded
    @POST("client.action")
    fun jdBeanHome(@FieldMap body: Map<String, String> = mapOf(
        "functionId" to "signBeanIndex",
        "appid" to "ld"
    )): Call<JdBeanHome>


    @GET("api?appid=jdsupermarket&functionId=smtg_sign&clientVersion=8.0.0&client=m&body=%7B%7D")
    @Headers("Origin:https://jdsupermarket.jd.com")
    fun jdStore(): Call<JdStore>

    /**
     * 好像需要jrCookie, 暂停
     */
    fun jrSteel(@Url url: String = "https://ms.jr.jd.com/gw/generic/hy/h5/m/appSign?")

    @GET("client.action?functionId=wheelSurfIndex&body=%7B%22actId%22%3A%22jgpqtzjhvaoym%22%2C%22appSource%22%3A%22jdhome%22%7D&appid=ld")
    fun jdTurn(): Call<JdTurn>

    @FormUrlEncoded
    @GET("client.action?functionId=lotteryDraw&appid=ld")
    fun jdTurnSign(@FieldMap body: Map<String, String>): Call<JdTurnSign>

    @Deprecated("重新抓包")
    @FormUrlEncoded
    @POST("client.action?functionId=partitionJdSgin")
    fun jdFlashSale(@FieldMap body: Map<String, String> = mapOf(
        "body" to "{\"version\":\"v2\"}",
        "functionId" to "partitionJdSgin",
        "client" to "apple",
        "clientVersion" to "9.0.8",
        "openudid" to "1fce88cd05c42fe2b054e846f11bdf33f016d676",
        "sign" to "6768e2cf625427615dd89649dd367d41",
        "st" to "1597248593305",
        "sv" to "121"
    )): Call<Any>

    @Deprecated("活动太火爆")
    @FormUrlEncoded
    @POST("client.action")
    fun jdCash(@FieldMap body: Map<String, String> = mapOf(
        "body" to "{\"pageClickKey\":\"CouponCenter\",\"eid\":\"O5X6JYMZTXIEX4VBCBWEM5PTIZV6HXH7M3AI75EABM5GBZYVQKRGQJ5A2PPO5PSELSRMI72SYF4KTCB4NIU6AZQ3O6C3J7ZVEP3RVDFEBKVN2RER2GTQ\",\"shshshfpb\":\"v1\\/zMYRjEWKgYe+UiNwEvaVlrHBQGVwqLx4CsS9PH1s0s0Vs9AWk+7vr9KSHh3BQd5NTukznDTZnd75xHzonHnw==\",\"childActivityUrl\":\"openapp.jdmobile%3a%2f%2fvirtual%3fparams%3d%7b%5c%22category%5c%22%3a%5c%22jump%5c%22%2c%5c%22des%5c%22%3a%5c%22couponCenter%5c%22%7d\",\"monitorSource\":\"cc_sign_ios_index_config\"}",
        "functionId" to "ccSignInNew",
        "client" to "apple",
        "clientVersion" to "8.5.0",
        "d_brand" to "apple",
        "d_model" to "iPhone8,2",
        "openudid" to "1fce88cd05c42fe2b054e846f11bdf33f016d676",
        "scope" to "11",
        "screen" to "1242*2208",
        "sign" to "1cce8f76d53fc6093b45a466e93044da",
        "st" to "1581084035269",
        "sv" to "102"
    )): Call<JdCash>


    @Deprecated("配置异常")
    @FormUrlEncoded
    @GET("client.action")
    fun jdMagicCube(@FieldMap body: Map<String, String> = mapOf(
        "functionId" to "getNewsInteractionInfo",
        "appid" to "smfe",
        "body" to "{\"sign\":2}"
    )): Call<JdMagicCube>

    @GET
    @Headers("Referer:https://active.jd.com/forever/cashback/index")
    fun jdSubsidy(@Url url: String = "https://ms.jr.jd.com/gw/generic/uc/h5/m/signIn7"): Call<JdSubsidy>


}