package com.zipper.auto.api.api.jd.delay.bean

data class JdSubsidy(
    val channelEncrypt: Int,
    val resultCode: Int,
    val resultData: ResultData?,
    val resultMsg: String
){
    data class ResultData(
        val code: Int,
        val `data`: Data,
        val msg: String
    )

    data class Data(
        val continuityDays: Int,
        val thisAmount: Int,
        val thisAmountStr: Double
    )
}