package com.zipper.sign.gdbh

import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    val map = mapOf(
        "check" to "true",
        "field" to "member_id,member_name,member_role,invite_code,gender,wechat,qq,province,city,avatar,balance,credits,phone,platform,validity,fans,income,status,openid",
        "member_id" to "5822255",
        "platform" to "android",
        "timestamp" to "1635068086"
    )
    println(GdbhApi.sign("5822255","1635070549","android").toLowerCase())
    GdbhApi().testExecute()

}