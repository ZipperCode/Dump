package com.zipper.auto.api.api.jd.delay.bean

data class JdMagicCube(
    val result: Result
){

    data class Result(
        val code: Int,
        val hasFinalLottery: Int,
        val loginStatus: Int,
        val pin: String,
        val rType: Int,
        val taskPoolInfo: TaskPoolInfo,
        val toast: String
    )

    data class TaskPoolInfo(
        val taskList: List<Any>
    )
}