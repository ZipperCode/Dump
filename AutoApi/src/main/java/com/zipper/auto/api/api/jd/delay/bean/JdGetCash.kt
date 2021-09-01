package com.zipper.auto.api.api.jd.delay.bean

data class JdGetCash(
    val code: Int,
    val `data`: Data,
    val msg: String
){

    data class Data(
        val bizCode: Int,
        val bizMsg: String,
        val result: Result?,
        val success: Boolean
    )

    data class Result(
        val extraReward: ExtraReward,
        val pushResult: Int,
        val signCash: Double,
        val signStatus: Int,
        val white2Black: Boolean
    )

    data class ExtraReward(
        val couponInfos: List<Any>,
        val extraCash: Double,
        val extraType: Int,
        val redPack: Double,
        val times: Int
    )

}