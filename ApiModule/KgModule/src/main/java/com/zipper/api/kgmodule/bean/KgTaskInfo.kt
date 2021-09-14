package com.zipper.api.kgmodule.bean

data class KgTaskInfo(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val status: Int = 0
){
    data class Data(
        val account: Account = Account(),
        val base: Base = Base(),
        val extend: Extend? = null,
        val state: State = State()
    )

    data class Account(
        val balance_coins: Int = 0,
        val mark: Int = 0,
        val total_coins: Int = 0
    )

    data class Extend(
        val starttime: Long = 0,
        val t_code: String = "",
        val id: String = "",
        val invite_count: Int = 0,
        val uuid: String = "-",
        val android_clientver: Int = 0,
        val lasttime: Long = 0,
        val c_code: String = "",
        val userid: String = "",
        val m_code: String = ""
    )

    data class State(
        val has_withdrawn: Boolean = false,
        val is_new_user: Boolean = false
    )

    data class Base(
        val header: String = "",
        val nickname: String = "",
        val userid: Int = 0
    )


}