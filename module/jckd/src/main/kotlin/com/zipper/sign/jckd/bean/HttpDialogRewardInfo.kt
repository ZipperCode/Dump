package com.zipper.sign.jckd.bean


data class HttpDialogRewardInfo(
    val action: String = "",
    val button: HttpDialogRewardButtonInfo = HttpDialogRewardButtonInfo(),
    val button2: HttpDialogRewardButtonInfo = HttpDialogRewardButtonInfo(),
    val desc: String = "",
    val dialog_type: String = "",
    val extra: String = "",
    val img_ad: List<DialogInfo.DialogInfoAd.CommonAdModel> = listOf(),
    val index: String = "",
    val isNewVersionRewardVideo: Boolean = false,
    val isShowCoutTime: Boolean = false,
    val score: String = "",
    val second: Long = 5,
    val tips: String = "",
    val title: String = "",
    val video_ad: List<DialogInfo.DialogInfoAd.CommonAdModel> = listOf()
) {

    data class HttpDialogRewardButtonInfo(
        var reward_action: String = "",
        var send_reward_action: String = "",
        var share_content: String = "",
        var share_url: String = "",
    ){

    }
}