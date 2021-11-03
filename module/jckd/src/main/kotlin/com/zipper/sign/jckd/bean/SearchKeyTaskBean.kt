package com.zipper.sign.jckd.bean

data class SearchKeyTaskBean(
    val billname: String = "",
    val enable_js_inject: Boolean = false,
    val finished_task: Int = 0,
    val guide_types: List<String> = listOf(),
    val keywords: List<Keyword> = listOf(),
    val result_stay_guide_duration: Int = 0,
    val reward_num: Int = 0,
    val search_match_regexp: List<String> = listOf(),
    val simple_guide: String = "",
    val target_times: Int = 0,
    val task_detail: String = "",
    val task_limit: Int = 0,
    val task_per_reward: Int = 0,
    val trace_id: String = "",
    val with_simple_guide: Boolean = false
) {
    data class Keyword(
        val type: String = "",
        val url: String = "",
        val value: String = ""
    )
}