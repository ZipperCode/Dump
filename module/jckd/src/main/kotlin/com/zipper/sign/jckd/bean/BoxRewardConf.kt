package com.zipper.sign.jckd.bean

data class BoxRewardConf(
    val list: List<Box> = listOf(),
    val num: Int = 0
) {
    data class Box(
        val id: Int = 0,
        val num: Int = 0,
        val score: Int = 0,
        val status: Int = 0
    )
}