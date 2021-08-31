package com.zipper.auto.api.api.jd.delay.bean

data class JdTurn(
    val code: String,
    val `data`: Data
){

    data class Data(
        val activityState: Int,
        val beanIndex: String,
        val encrytPin: String,
        val helpPeopleCount: Int,
        val helpState: String,
        val isCanShare: Boolean,
        val lotteryCode: String,
        val lotteryCount: String,
        val myAward: String,
        val prizeInfo: List<PrizeInfo>,
        val ruleLink: String,
        val shareBtImage: String,
        val shareBtTitle: String,
        val shareTitle: String,
        val winnerList: List<Winner>
    )

    data class PrizeInfo(
        val prizeId: String,
        val prizeImage: String,
        val prizeName: String,
        val prizeNumber: String,
        val prizeType: String
    )

    data class Winner(
        val prizeName: String,
        val userPin: String
    )
}