package com.zipper.core.exception

import java.lang.Exception

class AppException : Exception{

    var code: String? = null

    var realExpMessage: String? = null

    constructor(message: String): super(message)

    constructor(code: String?, message: String): super(message){
        this.code = code
    }

    constructor(message: String, cause: Throwable?): super(message, cause)

    constructor(message: String, realExpMessage:String?, cause: Throwable?): super(message, cause){
        this.realExpMessage = realExpMessage
    }
}