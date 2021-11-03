package com.zipper.sign.gdbh

import com.zipper.sign.core.*
import com.zipper.sign.core.base.BaseApi
import com.zipper.sign.core.ext.EncodeMode
import com.zipper.sign.core.ext.md5
import com.zipper.sign.core.interceptor.LoggerInterceptor
import com.zipper.sign.core.interceptor.NewInterceptor
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request

class GdbhApi : BaseApi("https://proxy.guodongbaohe.com/") {

    private val gdbhService: GdbhService = create()
    private var rewardCoin = 0
    private var userId: String = ""
    private var token: String = ""
    private var agent: String = " JellyBox/3.8.4 (Android, MI 6, 11)"
    private var platform: String = "android"


    override suspend fun execute() {
        signIn()
        video()
        log("任务执行完成，总共获得${rewardCoin}金币")
    }

    override suspend fun testExecute() {
        userId = "5822255"
        token = "e58487fc28acb7b1cd8b9c1d6683ac26"
        execute()
    }

    private suspend fun signIn() {
        log("开始执行签到")
        catchExp {
            val body = gdbhService.checkIn()
            if (body.status == 0) {
                if (body.result is String) {
                    rewardCoin += body.result.toInt() ?: 0
                }
            } else {
                log("签到失败：$body")
            }
        }
        log("签到执行完成")
        delaySecond(5)
    }

    private suspend fun video() {
        log("开始做看视频任务")
        for (i in 0 until 6) {
            catchExp {
                val body = gdbhService.award()
                if (body.status == 0) {
                    if (body.result is String) {
                        log("执行第${i + 1}次广告任务完成，获得 ${body.result} 个金币，等待10秒")
                        rewardCoin += body.result.toInt() ?: 0
                    }
                } else {
                    log("执行第${i + 1}次任务失败：${body.result}")
                }
                delayRandomSecond(2, 30)
            }
        }
        log("看视频任务执行完成")
    }

    private fun log(msg: String) {
        L.e("果冻宝盒-$userId", msg)
    }


    companion object {
        fun sign(memberId: String, timestamp: String, platform: String): String {
            return "member_id=${memberId}&platform=${platform}&timestamp=${timestamp}&faf78c39388faeaa49c305804bbc1119".md5(
                EncodeMode.HEX
            ).toLowerCase()
        }
    }

    override fun getInterceptors(): List<Interceptor> {
        return listOf(
            object : NewInterceptor() {
                override fun newRequest(oldRequest: Request): Request {
                    val newRequest = oldRequest.newBuilder()
                    newRequest
                        .addHeader("x-userid", userId)
                        .addHeader("x-appid", "2102202714")
                        .addHeader("x-devid", "No-dev")
                        .addHeader("x-nettype", "WIFI")
                        .addHeader("x-agent", agent)
                        .addHeader("x-platform", platform)
                        .addHeader("x-devtype", "no")
                        .addHeader("x-token", token)
                        .addHeader("User-agent", "okhttp/3.14.9")

                    val timestamp = secondTimeStr
                    val signature = sign(userId, timestamp, platform)
                    if (oldRequest.method.equals("GET", ignoreCase = true)) {
                        val newUrl = oldRequest.url.newBuilder()
                            .addEncodedQueryParameter("member_id", userId)
                            .addEncodedQueryParameter("platform", platform)
                            .addEncodedQueryParameter("timestamp", timestamp)
                            .addEncodedQueryParameter("signature", signature)
                            .build()
                        newRequest.url(newUrl)
                    } else if (oldRequest.method.equals("POST", ignoreCase = true)) {
                        if (oldRequest.body is FormBody) {
                            val body = oldRequest.body as FormBody
                            val newBodyBuilder = FormBody.Builder()
                            for (i in 0 until body.size) {
                                newBodyBuilder.addEncoded(body.encodedName(i), body.encodedValue(i))
                            }

                            newBodyBuilder.addEncoded("member_id", userId)
                                .addEncoded("platform", platform)
                                .addEncoded("timestamp", timestamp)
                                .addEncoded("signature", signature)
                            newRequest.post(newBodyBuilder.build())
                        }
                    }

                    return newRequest.build()
                }
            }
        )
    }
}