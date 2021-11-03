package com.zipper.sign.jckd.bean

data class AdClick(
    val banner_id: Int,
    val read_num: Int,
    val see_num: Int,
    val comtele_state: Int = 0,
    val score: Int
) {
}