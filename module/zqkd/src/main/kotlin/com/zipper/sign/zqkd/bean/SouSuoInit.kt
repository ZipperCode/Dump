package com.zipper.sign.zqkd.bean

data class SouSuoInit(
    val goods: List<Any> = listOf(),
    val search: List<Search> = listOf(),
    val time: Int = 0
) {
    data class Search(
        val action: Int = 0,
        val but: String = "",
        val desc: String = "",
        val img: String = "",
        val `param`: Param = Param(),
        val r_action: String = "",
        val ratio: String = "",
        val score: Int = 0,
        val tab: Int = 0,
        val title: String = ""
    ) {
        data class Param(
            val pid: String = "",
            val uid: Int = 0
        )
    }
}