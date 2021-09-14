package com.zipper.api.kgmodule.bean

data class KgTaskSignon(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val is_double: Boolean = false,
    val status: Int = 0,
    val taskid: Int = 0
){
    data class Data(
        val double_code: String? = "",
        val list: List<State> = listOf()
    )

    data class State(
        val award_coins: Int = 0,
        val award_vips: Int = 0,
        val code: String = "",
        val state: Int = 0,
        val time: Int = 0,
        val today: Int = 0,
        val type: String = ""
    )
}