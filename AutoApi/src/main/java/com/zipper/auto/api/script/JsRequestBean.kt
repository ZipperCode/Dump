package com.zipper.auto.api.script

data class JsRequestBean(
    val url: String,
    val headers: Map<String,String> = emptyMap(),
    var body:String = "",
    val timeout: Long = 10000
)