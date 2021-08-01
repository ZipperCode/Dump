package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class NeckLaceHomeResult(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("rtn_code")
    val rtnCode: Int = -1,
    @SerializedName("rtn_msg")
    val rtnMsg: String = ""
)