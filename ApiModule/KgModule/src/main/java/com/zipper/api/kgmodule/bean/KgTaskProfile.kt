package com.zipper.api.kgmodule.bean

data class KgTaskProfile(
    val `data`: Data = Data(),
    val errcode: Int = 0,
    val error: String = "",
    val status: Int = 0
){
    data class Data(
        val extra_task: List<Any> = listOf(),
        val global: Global = Global(),
        val task: List<Task> = listOf()
    )

    data class Global(
        val account_balance: Int = 0,
        val coin_rmb_rate: Int = 0,
        val day_risk_coins: Int = 0,
        val day_risk_rate: Int = 0,
        val delay_withdraw_level: Int = 0,
        val invite_code_prefix: String = "",
        val invite_code_suffix: String = "",
        val invite_code_url: String = "",
        val invite_history_url: String = "",
        val invite_url: String = "",
        val invite_url_outside: String = "",
        val max_risk_coins: Int = 0,
        val month_risk_coins: Int = 0,
        val old_user_withdraw_levels: List<Int> = listOf(),
        val `open`: Int = 0,
        val revenue_rule: String = "",
        val withdraw_condition_option: Int = 0,
        val withdraw_daily_max_count: Int = 0,
        val withdraw_levels: List<Int> = listOf(),
        val withdraw_levels_limits: List<Int> = listOf(),
        val withdraw_levels_unlock_sign_days: List<Int> = listOf(),
        val withdraw_remark: String = "",
        val withdraw_url: String = "",
        val xw_coin_rmb_rate: Int = 0
    )

    data class Task(
        val _award_list: List<Award> = listOf(),
        val _gift_end_time: String = "",
        val _gifts: List<Gift> = listOf(),
        val accumulation_condition: AccumulationCondition = AccumulationCondition(),
        val android_adid: String = "",
        val award_coins: Int = 0,
        val award_extra_coins: Int = 0,
        val award_type: Int = 0,
        val deadline: String = "",
        val exchange_coins: Int = 0,
        val guide_button: String = "",
        val icon: String = "",
        val interval: Int = 0,
        val intro: String = "",
        val ios_adid: String = "",
        val limit_type: Int = 0,
        val max_done_count: Int = 0,
        val min_time: Int = 0,
        val module: Int = 0,
        val name: String = "",
        val newpack_coins: Int = 0,
        val newpack_end_at: String = "",
        val newpack_pos: Int = 0,
        val newpack_start_at: String = "",
        val `open`: Int = 0,
        val per_award_str: String = "",
        val per_task_str: String = "",
        val random_coins: String = "",
        val show_coins: Int = 0,
        val sort: Int = 0,
        val special_condition: SpecialCondition = SpecialCondition(),
        val start_time: String = "",
        val tag: String = "",
        val taskid: Int = 0,
        val vip_end_at: String = "",
        val vip_limit: Int = 0,
        val vip_setting: String = "",
        val vip_start_at: String = ""
    )

    data class AccumulationCondition(
        val awards_str: String = "",
        val days_per_round: Int = 0,
        val since_version: Int = 0,
        val version: Int = 0
    )

    data class Award(
        val award: Int = 0,
        val done: Boolean = false,
        val type: Int = 0
    )

    data class Gift(
        val id: String = "",
        val num: Int = 0,
        val type: String = ""
    )

    data class SpecialCondition(
        val channels_task_close: String = "",
        val clienver_task_close: String = ""
    )

}