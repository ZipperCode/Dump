package com.zipper.sign.zqkd.bean


data class DialogInfo(
    val desc: String = "",
    val dialog_type: String = "",
    val img_ad: DialogInfoAd = DialogInfoAd(),
    val img_two_ad: DialogInfoAd = DialogInfoAd(),
    val invate: DialogInfoAd = DialogInfoAd(),
    val score: String = "",
    val second: Long = 0,
    val title: String = "",
    val total_score: String = "",
    val turn_off: Int = 0,
    val video_action: String = "",
    val video_ad: DialogInfoAd = DialogInfoAd(),
){
    
    data class DialogInfoAd(
        val action: String = "",
        val ad: List<CommonAdModel> = listOf(),
        val article: List<CommonAdModel> = listOf(),
        val is_login: String = "",
        val is_wap: String = "",
        val radio_flag: Int = 0,
        val radios: List<Int> = listOf(),
        val rewardTitle: String = "",
        val rewardWhy: String = "",
        val source: String = "",
        val text: String = "",
        val title: String = "",
        val type: String = "",
        val url: String = "",
        val video: List<CommonAdModel> = listOf()
    ){
        data class CommonAdModel(
            val ad_show_type: Int = 0,
            val ad_type: String = "",
            val ad_weight: Int = 0,
            val app_id: String = "",
            val height: Int = 0,
            val position_id: String = "",
            val show_type: String = "",
            val sort: Int = 0
        ){

        }
    }
}
