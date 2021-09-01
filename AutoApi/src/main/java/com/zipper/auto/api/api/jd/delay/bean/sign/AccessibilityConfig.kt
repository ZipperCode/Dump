package com.zipper.auto.api.api.jd.delay.bean.sign

data class AccessibilityConfig(
    val attrs: List<List<String>>,
    val categoryLevel: String,
    val iconJumpUrl: String,
    val iconUrl: String,
    val immersiveStyleConfig: ImmersiveStyleConfig,
    val jumpProtocol: JumpProtocol,
    val leftIconType: String,
    val link_type: String,
    val searchBoxUnderTitle: String,
    val searchContext: String,
    val searchScope: String,
    val slideUpStyleConfig: SlideUpStyleConfig,
    val styleConfig: StyleConfig,
    val trueContext: String
)