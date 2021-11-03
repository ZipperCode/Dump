package com.zipper.sign.zqkd.bean

import com.google.gson.annotations.SerializedName

data class HomeTask(
    val action: String = "",
    val completedCount: Int = 0,
    val fromType: String = "",
    val is_login: String = "",
    @SerializedName(alternate = ["is_wap"], value = "jump_type")
    val jumpType: String = "",
    val param: String = "",
    @SerializedName(alternate = ["title"], value = "name")
    val title: String = "",
    val url: String = "",
    val score: Int = 0,
    val status: Int = 0,
    val text: String = "",
    val unit: String = ""
)
