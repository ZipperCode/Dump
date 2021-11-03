package com.zipper.sign.jckd

data class BaseResponse<T>(
    val code: Int? = 0,
    val `data`: T? = null,
    val msg: String? = "",
    val status: Int? = 0
) {
}