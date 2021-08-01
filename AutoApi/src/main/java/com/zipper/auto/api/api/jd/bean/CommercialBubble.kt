package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class CommercialBubble(
    @SerializedName("endTime")
    val endTime: Long = 0,
    @SerializedName("imgUrl")
    val imgUrl: String = "",
    @SerializedName("jumpUrl")
    val jumpUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("startTime")
    val startTime: Long = 0
)