package com.zipper.auto.api.api.jd.delay.bean

data class JdUserSign2(
    val currentTime: Long,
    val `data`: String,
    val errorCode: String,
    val success: Boolean
)