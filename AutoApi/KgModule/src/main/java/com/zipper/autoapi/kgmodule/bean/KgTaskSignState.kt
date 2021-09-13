package com.sign.demo.kg.bean

data class KgTaskSignState(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val status: Int = 0
){
    data class Data(
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