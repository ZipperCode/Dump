package com.zipper.sign.zqkd.bean

data class TaskCenterTaskInfo(
    val advance: List<Advance> = listOf(),
    val banner: List<Any> = listOf(),
    val box: Box = Box(),
    val daily: List<Daily> = listOf(),
    val heard: List<Heard> = listOf(),
    val info_new: InfoNew? = InfoNew(),
    val more_task: MoreTask = MoreTask(),
    val new: List<New> = listOf(),
    val task_promotion: TaskPromotion = TaskPromotion()
) {
    data class Advance(
        val action: String = "",
        val badge: String = "",
        val but: String = "",
        val `class`: String = "",
        val desc: String = "",
        val double: String = "",
        val event: String = "",
        val icon_type: String = "",
        val score: String = "",
        val share_num: Int = 0,
        val status: String = "",
        val title: String = "",
        val title_num: Int = 0,
        val title_total: Int = 0,
        val url: String = ""
    )

    data class Box(
        val available_score: Int = 0,
        val complete_num: Int = 0,
        val more_task_num: Int = 0,
        val nine: Nine = Nine(),
        val six: Six = Six(),
        val three: Three = Three(),
        val zero: Zero = Zero()
    ) {
        data class Nine(
            val more_num: Int = 0,
            val reward_action: String = "",
            val score: Int = 0,
            val status: Int = 0,
            val task_num: Int = 0
        )

        data class Six(
            val more_num: Int = 0,
            val reward_action: String = "",
            val score: Int = 0,
            val status: Int = 0,
            val task_num: Int = 0
        )

        data class Three(
            val more_num: Int = 0,
            val reward_action: String = "",
            val score: Int = 0,
            val status: Int = 0,
            val task_num: Int = 0
        )

        data class Zero(
            val reward_action: String = "",
            val score: Int = 0,
            val status: Int = 0,
            val sub_title: String = "",
            val title: String = "",
            val url: String = ""
        )
    }

    data class Daily(
        val action: String = "",
        val backtime: String = "",
        val banner_id: String = "",
        val but: String = "",
        val `class`: String = "",
        val complete: String = "",
        val ctype: String = "",
        val desc: String = "",
        val event: String = "",
        val id: String = "",
        val is_sync: String = "",
        val limit: String = "",
        val `param`: String = "",
        val reward_action: String = "",
        val score: String = "",
        val shareName: String = "",
        val share_desc: String = "",
        val share_title: String = "",
        val status: Int = 0,
        val thumb: String = "",
        val title: String = "",
        val title_num: String = "",
        val title_total: String = "0",
        val type: String = "",
        val uid: String = "",
        val url: String = ""
    )

    data class Heard(
        val action: String = "",
        val event: String = "",
        val is_anim: Int = 0,
        val jump_type: String = "",
        val logo: String = "",
        val minlogo: String = "",
        val title: String = "",
        val topIcon: String = "",
        val url: String = ""
    )

    data class InfoNew(
        val day: String = "",
        val label: String = "",
        val label_en: String = "",
        val list: List<News> = listOf(),
        val today_reward: Boolean = false
    ) {
        data class News(
            val action: String = "",
            val desc: String = "",
            val is_today: String = "",
            val jump_type: String = "",
            val `param`: String = "",
            val score: String = "0",
            val status: Int = 0
        )
    }

    data class MoreTask(
        val action: String = "",
        val jump_type: String = "",
        val url: String = ""
    )

    data class New(
        val action: String = "",
        val badge: String = "",
        val but: String = "",
        val `class`: String = "",
        val desc: String = "",
        val event: String = "",
        val jump_type: String = "",
        val list: String = "",
        val reward_action: String = "",
        val score: String = "",
        val status: String = "",
        val title: String = "",
        val title_num: String = "",
        val title_total: String = "",
        val url: String = ""
    )

    data class TaskPromotion(
        val day_double_score: Int = 0,
        val day_score: Int = 0,
        val is_double: Int = 0,
        val list: List<String> = listOf(),
        val speed: Int = 0,
        val total_score: Int = 0
    )
}