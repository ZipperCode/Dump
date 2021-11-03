package com.zipper.sign.jckd.bean

data class ArticleDetailConfigInfo(
    val account_avatar: String = "",
    val account_id: String = "",
    val account_name: String = "",
    val ban_comment: String = "",
    val bottom_share_desc: String = "",
    val bottom_share_item: BottomShareItem = BottomShareItem(),
    val bottom_share_status: Int = 0,
    val bottom_share_tips_text: String = "",
    val bottom_share_title_text: String = "",
    val circle_config: CircleConfig = CircleConfig(),
    val cmt_num: String = "",
    val collect_num: String = "",
    val comment_num: String = "",
    val course_image: String = "",
    val course_show: String = "",
    val course_url: String = "",
    val error_code: String = "",
    val favorite: String = "",
    val fireShare: FireShare = FireShare(),
    val h5_extra: String = "",
    val is_collect: Int = 0,
    val is_expire: Int = 0,
    val is_follow: Int = 0,
    val is_thumbs_up: Int = 0,
    val mPush_rid: String = "",
    val media_id: String = "",
    val message: String = "",
    val red_text: String = "",
    val rid: String = "",
    val share_log_text: String = "",
    val share_url: String = "",
    val success: Boolean = false,
    val third_way: Int = 0,
    val third_way_pyq: Int = 0
) {
    data class BottomShareItem(
        val bottom_share_desc: String = "",
        val bottom_share_status: Int = 0,
        val bottom_share_tips_text: String = "",
        val bottom_share_title_text: String = "",
        val is_thumbs_up: Int = 0
    )

    data class CircleConfig(
        val circle_bottom_text: String = "",
        val circle_hide_time: String = "",
        val circle_show_time: String = "",
        val circle_top_text: String = "",
        val circle_url: String = ""
    )

    data class FireShare(
        val action: String = "",
        val is_login: String = "",
        val is_wap: Int = 0,
        val score: Int = 0,
        val status: Int = 0,
        val url: String = ""
    )
}