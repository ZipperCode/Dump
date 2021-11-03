package com.zipper.sign.gdbh.bean

data class BaseResp<T>(
    val status: Int = 0,
    val result: T? = null
) {
}