package com.zipper.api.kgmodule.bean

data class KgTaskStateList(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val status: Int = 0
){
    data class Data(
        val list: List<StateList> = listOf()
    )

    data class StateList(
        val accumulation_cnt: Int? = 0,
        val done_count: Int = 0,
        val done_timelength: Int = 0,
        val is_show: Boolean = false,
        val is_subscribed: Boolean = false,
        val last_award_time: Int = 0,
        val lottery: Lottery = Lottery(),
        val max_done_count: Int = 0,
        val max_time_length: Int = 0,
        val next_award_time: Int = 0,
        val server_time: Int = 0,
        val state: Int = 0,
        val taskid: Int = 0,
        val total_done_count: Int = 0
    )

    data class Lottery(
        val chances: Int = 0
    )
}