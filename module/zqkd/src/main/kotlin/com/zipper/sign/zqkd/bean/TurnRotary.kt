package com.zipper.sign.zqkd.bean

data class TurnRotary(
    val ad: Ad = Ad(),
    val chestOpen: List<ChestOpen> = listOf(),
    val chest_ad: ChestAd = ChestAd(),
    val doubleMultiple: Int = 0,
    val doubleNum: Int = 0,
    val opened: Int = 0,
    val remainTurn: Int = 0,
    val score: Int = 0,
    val turnRewardType: String = ""
) {
    data class Ad(
        val article: List<Article> = listOf(),
        val banner_img: List<Any> = listOf(),
        val big_img: List<BigImg> = listOf(),
        val splash: List<Splash> = listOf(),
        val tt_video: List<Any> = listOf(),
        val video: List<Video> = listOf()
    ) {
        data class Article(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val limit_num: Int = 0,
            val position_id: String = "",
            val postid: String = "",
            val show_type: String = "",
            val sort: String = ""
        )

        data class BigImg(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val limit_num: Int = 0,
            val position_id: String = "",
            val postid: String = "",
            val show_type: String = "",
            val sort: String = ""
        )

        data class Splash(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val limit_num: Int = 0,
            val position_id: String = "",
            val postid: String = "",
            val show_type: String = "",
            val sort: String = ""
        )

        data class Video(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val limit_num: Int = 0,
            val position_id: String = "",
            val postid: String = "",
            val show_type: String = "",
            val sort: String = ""
        )
    }

    data class ChestOpen(
        val gold: String = "",
        val received: Int = 0,
        val times: String = ""
    )

    data class ChestAd(
        val article: List<Any> = listOf(),
        val banner_img: List<Any> = listOf(),
        val big_img: List<Any> = listOf(),
        val splash: List<Any> = listOf(),
        val tt_video: List<Any> = listOf(),
        val video: List<Video> = listOf()
    ) {
        data class Video(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val limit_num: Int = 0,
            val position_id: String = "",
            val postid: String = "",
            val show_type: String = "",
            val sort: String = ""
        )
    }
}