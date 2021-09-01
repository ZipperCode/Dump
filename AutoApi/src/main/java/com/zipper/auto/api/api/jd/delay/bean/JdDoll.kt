package com.zipper.auto.api.api.jd.delay.bean

data class JdDoll(
    val resultCode: Int,
    val resultData: ResultData,
    val resultMsg: String
){
    data class ResultData(
        val code: Int,
        val `data`: Data?,
        val msg: String
    )

    data class Data(
        val businessCode: String,
        val businessData: BusinessData,
        val businessMsg: String
    )

    data class BusinessData(
        val businessCode: String,
        val businessData: BusinessDataX?,
        val businessMsg: String,
        val createTime: String?,
        val awardListVo: List<AwardVo>?
    )

    data class BusinessDataX(
        val createTime: String,
        val awardListVo: List<AwardVo>
    )

    data class AwardVo(
        val count: Double,
        val name: String,
        val type: Int
    )
}