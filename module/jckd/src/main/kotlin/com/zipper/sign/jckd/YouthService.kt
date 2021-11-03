package com.zipper.sign.jckd

import com.zipper.sign.jckd.bean.*
import retrofit2.http.*

interface YouthService {
    /**
     * 抽奖
     */
    @FormUrlEncoded
    @Headers(
        "WebApi:1",
        "Referer: https://ant.xunsl.com/html/rotaryTable/index.html"
    )
    @POST("WebApi/RotaryTable/turnRotary")
    suspend fun turnRotary(
        @Field("_") time: Long = System.currentTimeMillis()
    ):BaseResponse<TurnRotary>
    @FormUrlEncoded
    @Headers(
        "WebApi:1",
        "Referer: https://ant.xunsl.com/html/rotaryTable/index.html"
    )
    @POST("WebApi/RotaryTable/chestReward")
    suspend fun chestReward(
        @Query("_") time: Long = System.currentTimeMillis(),
        @Field("num") num: Int = 1
    ):BaseResponse<Map<String,String>>

    @Headers("WebApi:1",
    "Referer:http://ant.xunsl.com/h5/20190527searchListNew/")
    @GET("/WebApi/SousuoNew/init")
    suspend fun sousuoNewInit(
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<SouSuoInit>

    @Headers(
        "xiangKanWang:api.xiangkanwang.com"
    )
    @GET("v1/setting")
    suspend fun setting(
        @Query("uid") uid: String,
        @Query("pid") pid: String,
        @Query("key") key: String,
        @Query("fresh") fresh: Int = 0,
    ): SearchKeyTaskBean


    @Headers(
        "WebApi:1",
        "Referer: http://ant.xunsl.com/h5/20190527watchMoney/"
    )
    @GET("webApi/Nameless/getTaskBrowse")
    suspend fun getTaskBrowse(
        @Query("type") type: String = "0",
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<BrownTaskList>

    @Headers(
        "WebApi:1",
        "Referer: http://ant.xunsl.com/h5/20190527watchMoney/"
    )
    @GET("WebApi/Nameless/getBoxRewardConf")
    suspend fun getBoxRewardConf(
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<BoxRewardConf>

    @Headers(
        "WebApi:1",
        "Referer: http://ant.xunsl.com/h5/20190527watchMoney/"
    )
    @GET("WebApi/Nameless/getBoxReward")
    suspend fun getBoxReward(
        @Query("id") id:Int,
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<String>
}