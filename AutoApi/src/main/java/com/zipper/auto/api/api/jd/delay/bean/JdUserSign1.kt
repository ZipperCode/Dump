package com.zipper.auto.api.api.jd.delay.bean

data class JdUserSign1(
    val btnText: String,
    val channelPoint: ChannelPoint,
    val code: String,
    val list: List<ListState>,
    val msg: String,
    val noAwardTxt: String,
    val returnMsg: String,
    val signText: String,
    val statistics: String,
    val subCode: String,
    val subCodeMsg: String,
    val transParam: String
){
    data class ChannelPoint(
        val babelChannel: String,
        val pageId: String
    )

    data class ListState(
        val state: Int,
        val text: String
    )
}