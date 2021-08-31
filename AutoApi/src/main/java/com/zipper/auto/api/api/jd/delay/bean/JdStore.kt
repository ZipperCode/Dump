package com.zipper.auto.api.api.jd.delay.bean

data class JdStore(
    val code: Int,
    val `data`: Data,
    val msg: String
){
    data class Data(
        val bizCode: Int,
        val bizMsg: String,
        val success: Boolean
    )
}