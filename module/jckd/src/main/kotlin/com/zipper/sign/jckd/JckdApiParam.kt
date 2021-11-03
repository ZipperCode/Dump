package com.zipper.sign.jckd

data class JckdApiParam(
    val uid: String,
    val zqkeyId: String,
    val zqkey: String,
    val token: String,
    val userName: String,
    val withDrawMoney: String = "0.3"
)
