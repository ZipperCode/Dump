package com.zipper.auto.api.script

data class HttpResponseBean(
    val status: Int,
    val json: String
){
    override fun toString(): String {
        return "HttpResponseBean(status=$status, json='$json')"
    }
}
