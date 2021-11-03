package com.zipper.sign.zqkd

import com.zipper.sign.zqkd.bean.*
import retrofit2.http.*

interface YouthService {
    /**
     * 抽奖
     */
    @FormUrlEncoded
    @Headers(
        "kdYouth:kd.youth.cn",
        "Referer: https://kd.youth.cn/html/rotaryTable/index.html"
    )
    @POST("WebApi/RotaryTable/turnRotary")
    suspend fun turnRotary(
        @Field("_") time: Long = System.currentTimeMillis()
    ):BaseResponse<TurnRotary>
    @FormUrlEncoded
    @Headers(
        "kdYouth:kd.youth.cn",
        "Referer: https://kd.youth.cn/html/rotaryTable/index.html"
    )
    @POST("WebApi/RotaryTable/chestReward")
    suspend fun chestReward(
        @Query("_") time: Long = System.currentTimeMillis(),
        @Field("num") num: Int = 1
    ):BaseResponse<Map<String,String>>

    @Headers("kdYouth:kd.youth.cn",
    "Referer:https://kd.youth.cn/h5/20190707searchListNew/")
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
        "kdYouth:kd.youth.cn",
        "Referer: https://kd.youth.cn/h5/20190527watchMoney/"
    )
    @GET("webApi/Nameless/getTaskBrowse")
    suspend fun getTaskBrowse(
        @Query("type") type: String = "0",
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<BrownTaskList>

    @Headers(
        "kdYouth:kd.youth.cn",
        "Referer: https://kd.youth.cn/h5/20190527watchMoney/"
    )
    @GET("WebApi/Nameless/getBoxRewardConf")
    suspend fun getBoxRewardConf(
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<BoxRewardConf>

    @Headers(
        "kdYouth:kd.youth.cn",
        "Referer: https://kd.youth.cn/h5/20190527watchMoney/"
    )
    @GET("WebApi/Nameless/getBoxReward")
    suspend fun getBoxReward(
        @Query("id") id:Int,
        @Query("request_time") requestTime: Long = System.currentTimeMillis()
    ): BaseResponse<String>
}