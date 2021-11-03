package com.zipper.sign.zqkd

data class ZqkdApiParam(
    val uid: String,
    val zqkeyId: String,
    val zqkey: String,
    val token: String,
    val userName: String,
    val withDrawMoney: String = "0.3"
)
