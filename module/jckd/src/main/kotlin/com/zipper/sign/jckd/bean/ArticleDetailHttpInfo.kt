package com.zipper.sign.jckd.bean

data class ArticleDetailHttpInfo(
    val account_avatar: String = "",
    val account_id: Int = 0,
    val account_name: String = "",
    val catid: Int = 0,
    val catname: String = "",
    val content: String = "",
    val ctype: Int = 0,
    val h5_extra: H5Extra = H5Extra(),
    val id: Int = 0,
    val input_time: String = "",
    val title: String = ""
){
    class H5Extra
}