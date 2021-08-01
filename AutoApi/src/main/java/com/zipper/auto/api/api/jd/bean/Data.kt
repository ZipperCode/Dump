package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("biz_code")
    val bizCode: Int = -1,
    @SerializedName("biz_msg")
    val bizMsg: String = "",
    @SerializedName("result")
    val result: Result = Result()
)