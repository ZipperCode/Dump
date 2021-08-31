package com.zipper.auto.api.api.jd.delay.bean

data class JdCash(
    val busiCode: String,
    val code: String,
    val message: String,
    val result: Result,
    val success: Boolean
){
    data class Result(
        val signResult: SignResult
    )

    data class SignResult(
        val bizCode: String,
        val errCode: Any,
        val msg: String,
        val sessionId: Any,
        val showCode: String,
        val signData: Any,
        val success: Boolean
    )
}