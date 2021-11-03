package com.zipper.sign.jckd.bean

data class BrownTaskList(
    val banner_urls: String = "",
    val can_num: Int = 0,
    val can_score: Int = 0,
    val list: List<Task> = listOf(),
    val score: Int = 0,
    val uid: Int = 0
) {
    data class Task(
        val backtime: String = "",
        val banner_id: String = "",
        val complete: Int = 0,
        val ctype: String = "",
        val desc: String = "",
        val id: String = "",
        val limit: String = "",
        val name: String = "",
        val score: Int = 0,
        val shareName: String = "",
        val share_desc: String = "",
        val share_title: String = "",
        val status: Int = 0,
        val thumb: String = "",
        val title: String = "",
        val type: String = "",
        val uid: Int = 0,
        val url: String = ""
    )
}