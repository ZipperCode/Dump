package com.zipper.sign.jckd

data class BaseResponseModel<T>(
    var banners: String = "",
    var error_code: String = "",
    var flag: Int = 0,
    var has_next: Int = 0,
    var hasnext: Int = 0,
    var items: T? = null,
    var message: String = "",
    var score: String = "",
    var server_time: String = "",
    var share: String = "",
    var step: Int = 0,
    var success: Boolean = false,
    var timestamp: String = "",
    var type: Int = 0
) {
}