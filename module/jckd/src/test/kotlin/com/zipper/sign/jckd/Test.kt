package com.zipper.sign.jckd

import kotlinx.coroutines.runBlocking
import java.util.*

fun main() = runBlocking {
    val scanner = Scanner(System.`in`)
    while (true){
        println("选择请求/响应（1/2）:")
        val type = scanner.nextInt()
        if (type == 1){
            println("请输入请求体：")
            val body = scanner.next()
            println("请求体解密结果为:")
            JckdApi.decryptReq(body).split("&").forEach { println(it)  }
        }else{
            println("请输入响应体：")
            val body = scanner.next()
            val str = JckdApi.decryptResp(body)
            println("响应体解密结果为：\n$str")
        }
    }
}