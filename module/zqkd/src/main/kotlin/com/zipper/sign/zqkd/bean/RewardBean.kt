package com.zipper.sign.zqkd.bean

data class RewardBean(
    val bottom_data: BottomData = BottomData(),
    val click_url: String = "",
    val complete: Int = 0,
    val ctype: Int = 0,
    val day_visit_num: String = "",
    val dialog: Int = 0,
    val is_bsg: Int = 0,
    val is_popup: Int = 0,
    val is_progress: Int = 0,
    val is_short_video_progress: Int = 0,
    val is_short_video_show: Int = 0,
    val is_show: Int = 0,
    val is_show_read_reward: Int = 0,
    val is_video_progress: Int = 0,
    val is_video_show: Int = 0,
    val is_welfare_score: Int = 0,
    val n_money: String = "",
    val n_score: Int = 0,
    val pass_popup: PassPopup = PassPopup(),
    val people: Int = 0,
    val read_reward: ReadReward = ReadReward(),
    val read_score: Int = 0,
    val readtime: Int = 0,
    val s_money: String = "",
    val s_score: Int = 0,
    val short_video_complete: Int = 0,
    val time: String = "",
    val times_title: String = "",
    val total_score: Int = 0,
    val version: Int = 0,
    val video: Video = Video(),
    val video_complete: Int = 0
) {
    data class BottomData(
        val end: Int = 0,
        val is_show_share: Int = 0,
        val next_num: Int = 0,
        val read_num: Int = 0,
        val remain_num: Int = 0,
        val score: Int = 0
    )

    data class PassPopup(
        val score: Int = 0,
        val status: Int = 0,
        val title: String = "",
        val url: String = ""
    )

    data class ReadReward(
        val status: Int = 0
    )

    data class Video(
        val status: Int = 0
    )
}