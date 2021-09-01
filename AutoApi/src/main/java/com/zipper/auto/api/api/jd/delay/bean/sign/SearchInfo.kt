package com.zipper.auto.api.api.jd.delay.bean.sign

data class SearchInfo(
    val attrs: List<List<String>>,
    val categoryLevel: String,
    val jumpSearch: JumpSearch,
    val searchContext: String,
    val searchScope: String,
    val trueContext: String
)