package com.zipper.core.model

import java.lang.Exception

sealed class DataResult<T>{
    data class Success<T>(val data: T): DataResult<T>()

    data class Error(val exception: Exception): DataResult<Nothing>()

}
