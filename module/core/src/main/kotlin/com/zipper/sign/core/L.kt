package com.zipper.sign.core

object L {
    fun d(message: String) {
        println(message)
    }

    fun e(message: String) {
        System.err.println(message)
    }

    fun d(tag: String, message: String){
        println("【$tag】 ==> $message")
    }

    fun e(tag: String, message: String){
        System.err.println("【$tag】 ==> $message")
    }
}