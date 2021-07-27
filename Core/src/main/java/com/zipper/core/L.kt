package com.zipper.core

import android.util.Log

object L {

    private const val TAG: String = "Dump_L"

    fun d() {
        log(Log.DEBUG, "-", "-")
    }

    fun d(msg: String) {
        log(Log.DEBUG, "-", msg)
    }

    fun d(tag: String, msg: String) {
        log(Log.DEBUG, tag, msg)
    }

    fun e( msg: String) {
        log(Log.ERROR, "-", msg)
    }

    fun e(tag: String, msg: String) {
        log(Log.ERROR, tag, msg)
    }

    fun e(e: Exception) {
        Log.e(TAG, "", e)
    }

    private fun log(type: Int, tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            val targetTrace = Thread.currentThread().stackTrace[4]
            val content = "[$tag] >> ${targetTrace.className}-${targetTrace.methodName} : $msg"
            when (type) {
                Log.DEBUG -> Log.d(TAG, content)
                Log.ERROR -> Log.e(TAG, content)
                Log.INFO -> Log.i(TAG, content)
                Log.VERBOSE -> Log.v(TAG, content)
                Log.WARN -> Log.w(TAG, content)
            }
        }
    }
}