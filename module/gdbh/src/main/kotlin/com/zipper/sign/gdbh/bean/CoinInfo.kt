package com.zipper.sign.gdbh.bean

data class CoinInfo(
    val avatar: String = "",
    val balance: String = "",
    val checkin: Checkin = Checkin(),
    val city: String = "",
    val created_date: String = "",
    val created_time: String = "",
    val credits: String = "",
    val fans: String = "",
    val gender: String = "",
    val income: String = "",
    val invite_code: String = "",
    val logined_time: String = "",
    val member_id: String = "",
    val member_name: String = "",
    val member_role: String = "",
    val openid: String = "",
    val phone: String = "",
    val platform: String = "",
    val province: String = "",
    val qq: String = "",
    val remark: Any? = null,
    val status: String = "",
    val unionid: String = "",
    val updated_time: Any? = null,
    val validity: String = "",
    val wechat: String = ""
) {
    data class Checkin(
        val check_id: Boolean = false,
        val day: Int = 0,
        val last_timestamp: Int = 0,
        val total_day: Int = 0
    )
}