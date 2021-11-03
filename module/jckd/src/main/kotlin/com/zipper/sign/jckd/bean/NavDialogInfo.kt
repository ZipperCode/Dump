package com.zipper.sign.jckd.bean

data class NavDialogInfo(
    val button: Button = Button(),
    val desc: String = "",
    val prompt: String = "",
    val score: Int = 0,
    val score_text: String = "",
    val share_num: String = "",
    val status: Int = 0,
    val title: String = ""
) {
    data class Button(
        val action: String = "",
        val is_login: String = "",
        val is_wap: String = "",
        val name: String = "",
        val url: String = ""
    )
}