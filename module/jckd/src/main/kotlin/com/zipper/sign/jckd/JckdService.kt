package com.zipper.sign.jckd

import com.zipper.sign.jckd.bean.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface JckdService {

    @GET("v3/user/userinfo.json")
    suspend fun userInfo(): BaseResponseModel<UserInfo>
    @FormUrlEncoded
    @POST("/v5/Article/getVideoReward.json")
    suspend fun getVideoReward(@Field("action") str: String?): BaseResponseModel<DialogInfoConvert>

    @FormUrlEncoded
    @POST("v5/LiuLan/callback.json")
    suspend fun liuLan(@Field("task_id") str: String?): BaseResponseModel<HttpDialogRewardInfo>

    @POST("v5/Game/GameVideoReward.json")
    suspend fun gameVideoReward(): BaseResponseModel<DialogInfo>

    @POST("v5/article/getGoldenEggReward.json")
    suspend fun getGoldenEggReward(): BaseResponseModel<DialogInfo>

    @FormUrlEncoded
    @POST("v5/article/read.json")
    suspend fun readReward(@Field("id") str: String): ResponseBody

    @FormUrlEncoded
    @POST("v5/CommonReward/toGetReward.json")
    suspend fun toGetReward(
        @Field("action") action: String,
        @Field("index") index: String,
        @Field("video_id") video_id: String
    ): BaseResponseModel<HttpDialogRewardInfo>

    @FormUrlEncoded
    @POST("v5/CommonReward/toGetReward.json")
    suspend fun toGetReward2(
        @Field("action") action: String,
        @Field("param") param: String,
        @Field("extra") extra: String = ""
    ): BaseResponseModel<HttpDialogRewardInfo>

    @FormUrlEncoded
    @POST("/v5/CommonReward/toDouble.json")
    suspend fun toGetRewardSecond(
        @Field("action") action: String,
        @Field("index") index: String,
        @Field("video_id") video_id: String
    ): BaseResponseModel<HttpDialogRewardInfo>

    @POST("v5/RotaryTable/toTurnDouble.json")
    suspend fun toTurnDouble(): BaseResponseModel<DialogInfoConvert>

    @FormUrlEncoded
    @POST("v5/Sousuo/playEnd.json")
    suspend fun newAdlickend(@Field("task_id") task_id: String): BaseResponseModel<Map<String, String>>

    @FormUrlEncoded
    @POST("v5/Sousuo/playStart.json")
    suspend fun newAdlickstart(@Field("task_id") task_id: String?): ResponseBody

    @FormUrlEncoded
    @POST("v5/Sousuo/playStatus.json")
    suspend fun newBannerstatus(@Field("task_id") task_id: String?): ResponseBody

    @FormUrlEncoded
    @POST("v5/article/complete.json")
    suspend fun articleComplete(@Field("id") id: String, @Field("read_type") readType: String): BaseResponseModel<RewardBean>

    @GET("v17/NewTask/getTaskList.json")
    suspend fun getTaskCenterTaskInfo(): BaseResponseModel<TaskCenterTaskInfo>

    @GET("v17/NewTask/getSign.json")
    suspend fun getTaskCenterSignInfo(@Query("uid") str: String?): BaseResponseModel<TaskCenterSignInfo>

    @GET("v17/NewTask/back.json")
    suspend fun reportTaskBack(@Query("action") str: String?): BaseResponseModel<Boolean>

    @GET("V17/NewTask/recordNum.json")
    suspend fun sendRecordNum(): BaseResponseModel<ArrayList<Any>>

    /**
     * 翻倍
     */
    @GET("v17/NewTask/setJoinTaskPromotion.json")
    suspend fun setWeekCoinDoubleTaskPromotion(): BaseResponseModel<Any>

    @GET("v17/NewTask/setShareInvite.json")
    suspend fun setShareInvite(): BaseResponseModel<Any?>

    @FormUrlEncoded
    @POST("v17/Ad/getReward.json")
    suspend fun getReward(@Field("reward_type") i: Int): BaseResponseModel<DialogInfo>

    // TODO unknown
    @GET("V17/Ximalaya/sendMusicMoneyReward.json")
    suspend fun acquireSongReward(@Query("level") level: Int): BaseResponseModel<String>

    @FormUrlEncoded
    @POST("V17/Ximalaya/xiMalayaComplete.json")
    suspend fun getSongReward(@Field("id") id: Long): BaseResponseModel<RewardBean>

    @GET("V17/Ximalaya/getMusicMoneyList.json")
    suspend fun getSongRewardList(): BaseResponseModel<SongRewardListModel>

    @FormUrlEncoded
    @POST("v17/Rvideo/getAds.json")
    suspend fun getRewardVideoAd(
        @Field("action") action: String,
        @Field("extra") extra: String
    ): BaseResponseModel<ArrayList<DialogInfo.DialogInfoAd.CommonAdModel>>

    @GET("v17/Article/readReward.json")
    suspend fun readReward(): BaseResponseModel<HomeTask>

    @POST("v14/user/getTimingRedReward.json")
    suspend fun getTimingRedReward(): BaseResponseModel<DialogInfo>

    @FormUrlEncoded
    @POST("v17/Rvideo/videoCallback.json")
    suspend fun videoCloseReward(
        @Field("action") action: String,
        @Field("extra") extra: String
    ): BaseResponseModel<HttpRewardInfo>

    @GET("v15/config/info.json")
    suspend fun getAppConfig(): BaseResponseModel<Map<String,Any>>

    @FormUrlEncoded
    @POST
    suspend fun getArticleContent(
        @Field("id") str: String,
        @Field("signature") str2: String,
        @Url url: String
    ): BaseResponseModel<ArticleDetailHttpInfo>

    /**
     * 微信转发
     * article_id=40327095
     * stype=wx
     * from=4
     */
    @FormUrlEncoded
    @POST("v6/article/share/put.json")
    suspend fun articleShare(
        @Field("article_id") article_id: String,
        @Field("stype") stype: String = "wx",
        @Field("from") from: Int = 4
    ): BaseResponseModel<NavDialogInfo>

    @GET("v5/article/info.json")
    suspend fun getNewArticleDetail(
        @Query("id") id: String,
        @Query("catid") catid: String,
        @Query("is_push") is_push: String
    ): Response<ArticleDetailConfigInfo>

    /**
     * 相关阅读
     */
    @FormUrlEncoded
    @POST
    @Deprecated("")
    suspend fun articleRelate(
        @Field("id") id: String,
        @Field("page") page: String,
        @Field("page_size") page_size: String,
        @Field("token") token: String,
        @Url url:String = "https://content.wkandian.com/v16/api/content/article/relate"
    ): BaseResponseModel<List<Any>>

    @GET("v3/article/lists.json")
    suspend  fun articleList(
        @Query("catid") catid: Int,
        @Query("video_catid") video_catid: Int = 1453,
        @Query("op") op: String = "0",
        @Query("behot_time") behot_time: String = System.currentTimeMillis().toString(),
        @Query("oid") oid: String = "0"
    ): BaseResponseModel<List<MainArticleInfo>>

    @GET("v3/article/lists.json")
    suspend fun videoList(
        @Query("video_catid") video_catid: Int,
        @Query("op") op: String = "0",
        @Query("behot_time") behot_time: String = System.currentTimeMillis().toString(),
        @Query("oid") oid: String = "0",
        @Query("catid") catid: Int = 1453
    ): BaseResponseModel<List<MainVideoInfo>>

    @FormUrlEncoded
    @POST
    suspend fun articleContent(
        @Field("id") id: String,
        @Field("signature") signature: String,
        @Url url: String = "https://content.wkandian.com/v16/api/content/article/content"
    ): BaseResponseModel<ArticleContent>

    @GET("v15/config/advertVideo.json")
    suspend fun advertVideo(
        @Query("position_id") position_id: Int = 3
    ): BaseResponseModel<Any>

    @GET("V17/NewTask/recordNum.json")
    suspend fun recordNum(): BaseResponseModel<Any>

    @Headers("User-Agent: Dalvik/2.1.0 (Linux; U; Android 11; MI 6 Build/RQ3A.210605.005)")
    @GET
    suspend fun todayHotWord(
        @Query("uid") uid: String,
        @Query("fresh") fresh: String = "0",
        @Query("key") key: String = "c8414341569dc5ad08294f8e11efbb48",
        @Query("pid") pid: String = "pols104",
        @Url url: String = "https://api.xiangkanwang.com/v1/setting"
    )

    @Headers("User-Agent:KDApp/2.4.1 (iPhone; iOS 14.7; Scale/3.00)")
    @FormUrlEncoded
    @POST("v5/task/browse_start.json")
    suspend fun browseStart(@Field("id")id: String): BaseResponseModel<Browse>

    @Headers("User-Agent:KDApp/2.4.1 (iPhone; iOS 14.7; Scale/3.00)")
    @FormUrlEncoded
    @POST("v5/task/browse_end.json")
    suspend fun browseEnd(@Field("id")id: String): BaseResponseModel<Browse>

    @FormUrlEncoded
    @POST("v5/nameless/adlickstart.json")
    suspend fun adClickStart(@Field("task_id")id: String): BaseResponseModel<AdClick>

    @FormUrlEncoded
    @POST("v5/nameless/bannerstatus.json")
    suspend fun bannerStatus(
        @Field("task_id") task_id: String
    ): BaseResponseModel<Any>

    @FormUrlEncoded
    @POST("v5/nameless/adlickend.json")
    suspend fun adClickEnd(
        @Field("task_id") task_id: String
    ): BaseResponseModel<AdClick>

    @FormUrlEncoded
    @POST("v5/wechat/withdraw2.json")
    suspend fun withDraw2(
        @Field("type")type: String,
        @Field("username")username: String,
        @Field("money")money: String,
    ): BaseResponseModel<Any>

}