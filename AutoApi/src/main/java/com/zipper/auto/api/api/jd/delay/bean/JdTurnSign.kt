package com.zipper.auto.api.api.jd.delay.bean

data class JdTurnSign(
    val code: String,
    val errorCode: String,
    val errorMessage: String,
    val data: Data?
){

    data class Data(
        val prizeSendNumber: String,
        val chances: String
    )
}