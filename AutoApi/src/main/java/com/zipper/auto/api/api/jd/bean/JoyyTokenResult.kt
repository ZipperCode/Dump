package com.zipper.auto.api.api.jd.bean

data class JoyyTokenResult(
    var code: Int = 0,
    var joyytoken: String? = null,
    var openMonitor: String? = null,
    var openPre: String? = null,
    var collectStatus: String? = null,
    var collect_vote: String? = null,
    var collect_rate: String? = null
)