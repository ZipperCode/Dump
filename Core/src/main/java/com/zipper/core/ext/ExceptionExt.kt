package com.zipper.core.ext

import com.zipper.core.exception.AppException
import retrofit2.HttpException
import java.lang.Exception
import java.lang.NullPointerException

fun Exception.handleException(): AppException {
    return when (this) {
        is AppException -> {
            return this
        }
        is NullPointerException -> {
            return AppException("运行时遇到了错误",message, this.cause)
        }
        is HttpException -> {
            return when (code()) {
                /*禁止访问*/ 403,/*找不到资源*/ 404 -> AppException(code().toString(), "网络访问异常，请重试")
                /*服务器错误*/500 -> AppException(code().toString(), "服务器错误")
                /*网络超时*/504 -> AppException(code().toString(), "网络连接超时")
                else -> AppException(code().toString(), "网络错误")
            }
        }
        else -> AppException("未知异常", message, this.cause)
    }
}