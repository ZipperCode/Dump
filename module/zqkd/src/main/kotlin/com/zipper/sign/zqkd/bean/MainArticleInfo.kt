package com.zipper.sign.zqkd.bean

data class MainArticleInfo(
    val account_avatar: String = "",
    val account_id: String = "",
    val account_name: String = "",
    val article_type: String = "",
    val behot_time: Long = 0,
    val catid: String = "",
    val catname: String = "",
    val cmt_num: String = "",
    val collect_num: String = "",
    val ctype: Int = 0,
    val description: String = "",
    val extra: List<String> = listOf(),
    val extra_data: ExtraData = ExtraData(),
    val id: String = "",
    val image_type: String = "",
    val input_time: String = "",
    val is_follow: Int = 0,
    val label_id: String = "",
    val media_id: String = "",
    val read_num: String = "",
    val share_num: String = "",
    val share_url: String = "",
    val signature: String = "",
    val tagid: String = "",
    val thumb: String = "",
    val title: String = "",
    val url: String = "",
    val wap_read_count: String = ""
) {
    data class ExtraData(
        val exp_id: String = "",
        val log_id: String = "",
        val retrieve_id: String = "",
        val strategy_id: String = ""
    )
}