package com.zipper.api.kgmodule.bean

data class KgTaskSubmit(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val is_double: Boolean? = false,
    val status: Int = 0,
    val taskid: Int = 0
){
    data class Data(
        val awards: Awards = Awards(),
        val double_code: String? = "",
        val extra: List<Any> = listOf(),
        val state: State = State()
    )

    data class Awards(
        val coins: Int = 0,
        val extra_coins: Int = 0,
        val time_coins: Int = 0
    )

    data class State(
        val done_count: Int = 0,
        val done_timelength: Int = 0,
        val is_show: Boolean = false,
        val is_subscribed: Boolean = false,
        val last_award_time: Int = 0,
        val max_done_count: Int = 0,
        val max_time_length: Int = 0,
        val next_award_time: Int = 0,
        val server_time: Int = 0,
        val taskid: Int = 0,
        val total_done_count: Int = 0
    )

    data class Gift(
        val can_excharge: Boolean = false,
        val coins: Int = 0,
        val gift_id: Int = 0,
        val gift_type: Int = 0,
        val icon: String = "",
        val name: String = "",
        val order_no: String = "",
        val remain_num: Int = 0,
        val sort: Int = 0,
        val total_num: Int = 0
    )
}