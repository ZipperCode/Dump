package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("alarmClockTips")
    val alarmClockTips: List<String> = listOf(),
    @SerializedName("commercialBubble")
    val commercialBubble: CommercialBubble = CommercialBubble(),
    @SerializedName("exchangeGiftConfigs")
    val exchangeGiftConfigs: List<ExchangeGiftConfig> = listOf(),
    @SerializedName("hideScoreDetail")
    val hideScoreDetail: Int = 0,
    @SerializedName("lastRequestTime")
    val lastRequestTime: String = "",
    @SerializedName("logined")
    val logined: Int = 0,
    @SerializedName("ruleUrl")
    val ruleUrl: String = "",
    @SerializedName("strongNotice")
    val strongNotice: String = "",
    @SerializedName("taskConfigVos")
    val taskConfigVos: List<TaskConfigVo> = listOf(),
    @SerializedName("taskCountOfFirstDisplay")
    val taskCountOfFirstDisplay: Int = 0,
    @SerializedName("taskCountdown")
    val taskCountdown: Boolean = false,
    @SerializedName("taskStyle")
    val taskStyle: Int = 0,
    @SerializedName("taskSubtitle")
    val taskSubtitle: String = ""
)