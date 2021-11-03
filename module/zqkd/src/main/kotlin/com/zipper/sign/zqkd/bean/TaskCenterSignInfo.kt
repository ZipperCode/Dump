package com.zipper.sign.zqkd.bean

data class TaskCenterSignInfo(
    val ad_list: List<Ad> = listOf(),
    val ad_list_source: String = "",
    val beread_red: BereadRed = BereadRed(),
    val every_sign_day: Int = 0,
    val invite_reward: Int = 0,
    val invite_reward_num: Int = 0,
    val is_bsg: Int = 0,
    val is_discontinue: Int = 0,
    val is_open_packet: Boolean = false,
    val is_reg_cur_day: Int = 0,
    val is_sign: Boolean = false,
    val red: Red = Red(),
    val red_url: String = "",
    val reward_action: String = "",
    val share_top_url: String = "",
    val sign: List<Sign> = listOf(),
    val sign_day: Int = 0,
    val sign_lucky_video: Int = 0,
    val sign_score: Int = 0,
    val surprise_red: SurpriseRed = SurpriseRed(),
    val swz: Swz = Swz(),
    val tomorrow_score: List<Int> = listOf(),
    val total_bank_money: Int = 0,
    val total_day: Int = 0,
    val total_sign_days: Int = 0,
    val user: User = User(),
    val zqdt: Zqdt = Zqdt()
) {
    data class Ad(
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

    data class BereadRed(
        val end_time: Int = 0,
        val next_time: Int = 0,
        val red_status: Int = 0
    )

    data class Red(
        val is_red: Int = 0,
        val is_show_tips: Int = 0,
        val nextRed: Int = 0
    )

    data class Sign(
        val dates: String = "",
        val day: Int = 0,
        val double: Double = 0.0,
        val is_double: Int = 0,
        val is_today: String = "",
        val score: List<Int> = listOf(),
        val show_tomorrow_tag: Int = 0,
        val status: Int = 0,
        val time: Int = 0
    )

    data class SurpriseRed(
        val action: String = "",
        val badge: String = "",
        val but: String = "",
        val `class`: String = "",
        val desc: String = "",
        val event: String = "",
        val icon_type: String = "",
        val jump_type: Int = 0,
        val score: String = "",
        val status: String = "",
        val title: String = "",
        val url: String = ""
    )

    data class Swz(
        val is_swz: Int = 0,
        val logo: String = "",
        val title: List<String> = listOf(),
        val url: String = ""
    )

    data class User(
        val avatar: String = "",
        val bank_score: Int = 0,
        val is_new_user: Int = 0,
        val is_one_withdraw: Int = 0,
        val is_withdraw: Int = 0,
        val list_url: String = "",
        val minimum_withdraw: Double = 0.0,
        val money: String = "",
        val nickname: String = "",
        val pass_mark: Int = 0,
        val score: String = "",
        val video_next_time: Int = 0,
        val video_num: Int = 0,
        val withdraw_type: Int = 0,
        val withdraw_url: String = ""
    )

    data class Zqdt(
        val but: String = "",
        val desc: String = "",
        val logo: String = "",
        val status: Int = 0,
        val title: List<String> = listOf(),
        val url: String = ""
    )
}