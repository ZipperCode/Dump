package com.zipper.core

import java.lang.Exception

sealed class DataResult<T>{
    class Success<T>(val data: T): DataResult<T>()

    class Failure(val exception: Exception): DataResult<Unit>()
}
