package com.zipper.auto.api.api.jd.delay.bean

data class JdShake(
    val `data`: Data,
    val success: Boolean,
    val resultCode: String?,
    val message: String?
){

    data class Data(
        val luckyBox: LuckyBox?,
        val prizeCoupon: PrizeCoupon?
    )

    data class LuckyBox(
        val costBeanCount: Int,
        val feeTimes: Int,
        val freeTimes: Int,
        val totalBeanCount: Int
    )

    data class PrizeCoupon(
        val batchId: Int,
        val beginTime: String,
        val couponKind: Int,
        val couponStyle: Int,
        val couponType: Int,
        val discount: Double,
        val endTime: String,
        val limitStr: String,
        val quota: Double,
        val shopId: Int,
        val venderId: Int
    )
}