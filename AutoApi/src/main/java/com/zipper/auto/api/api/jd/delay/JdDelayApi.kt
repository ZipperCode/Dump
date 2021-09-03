package com.zipper.auto.api.api.jd.delay

import android.content.Context
import com.zipper.auto.api.api.jd.JdBaseApi
import com.zipper.core.utils.L
import com.zipper.core.utils.SpUtil
import kotlinx.coroutines.*
import java.lang.Exception

class JdDelayApi(storeKey: String) : JdBaseApi(storeKey) {

    companion object {
        const val TAG: String = "JdDelayApi"
    }

    override fun domain(): String = "https://api.m.jd.com/"

    private val jdDelaySignService: JdDelaySignService = retrofit.create(JdDelaySignService::class.java)

    private val jdShopping = mapOf(
        "京东商城-内衣" to "4PgpL1xqPSW1sVXCJ3xopDbB1f69",
        "京东商城-卡包" to "7e5fRnma6RBATV9wNrGXJwihzcD",
        "京东商城-陪伴" to "kPM3Xedz1PBiGQjY4ZYGmeVvrts",
        "京东商城-鞋靴" to "4RXyb1W4Y986LJW8ToqMK14BdTD",
        "京东商城-童装" to "3Af6mZNcf5m795T8dtDVfDwWVNhJ",
        "京东商城-母婴" to "3BbAVGQPDd6vTyHYjmAutXrKAos6",
        "京东商城-数码" to "4SWjnZSCTHPYjE5T7j35rxxuMTb6",
        "京东商城-女装" to "DpSh7ma8JV7QAxSE2gJNro8Q2h9",
        "京东商城-图书" to "3SC6rw5iBg66qrXPGmZMqFDwcyXi",
        "京东商城-电竞" to "CHdHQhA5AYDXXQN9FLt3QUAPRsB",
        "京东商城-箱包" to "ZrH7gGAcEkY2gH8wXqyAPoQgk6t",
        "京东商城-校园" to "2QUxWHx5BSCNtnBDjtt5gZTq7zdZ",
        "京东商城-健康" to "w2oeK5yLdHqHvwef7SMMy4PL8LF",
        "京东商城-清洁" to "2Tjm6ay1ZbZ3v7UbriTj6kHy9dn6",
        "京东商城-个护" to "2tZssTgnQsiUqhmg5ooLSHY9XSeN",
        "京东商城-家电" to "3uvPyw1pwHARGgndatCXddLNUxHw",
        "京东商城-菜场" to "Wcu2LVCFMkBP3HraRvb7pgSpt64"
    )

    private val jdOther = mapOf(
        "京东失眠-补贴" to "UcyW9Znv3xeyixW1gofhW2DAoz4",
        "京东手机-小时" to "4Vh5ybVr98nfJgros5GwvXbmTUpg",
        "京东拍拍-二手" to "3S28janPLYmtFxypu37AYAGgivfp",
        "京东手机-小时" to "4Vh5ybVr98nfJgros5GwvXbmTUpg"
    )


    override suspend fun execute(context: Context) {

        if(!checkLogin()){
            L.d(TAG, "token 失效")
            return
        }

        catchExp {
            jdBeanHome() // 京东京豆
        }
        delay(100)
        catchExp {
            jdStore() // 京东超市
        }
        delay(100)
        catchExp {

        }
        catchExp {
            jdTurn() // 京东转盘
        }
        delay(100)
        catchExp {
            jdGetCash() // 京东领现金
        }
        delay(100)
        catchExp {
            jdSubsidy()
        }

        delay(100)
        catchExp {
            jdShake()
        }

        delay(100)
        catchExp {
            jdSecKilling()
        }

        delay(1000)

        for (entry in jdShopping) {
            catchExp {
                jdUserSignPre(entry.key, entry.value)
            }
            delay(1000)
        }
        catchExp {
            jdDoll("京东金贴-双签", "1DF13833F7")
        }

        delay(1000)

        for (entry in jdOther) {
            catchExp {
                jdUserSignPre(entry.key, entry.value)
            }
            delay(1000)
        }
        catchExp {
            jdDoll("金融京豆-双签", "F68B2C3E71", "", "jingdou")
        }
    }

    suspend fun checkLogin(): Boolean {
        val response = jdDelaySignService.totalBean()
        if (!response.isSuccessful) {
            L.d(TAG, "请求失败")
            return false
        }

        val body = response.body()

        isLogin =  (body?.retcode != "1001" || body.data != null) && body?.data?.userInfo != null
        return isLogin
    }

    suspend fun jdBeanHome() {
        val response = jdDelaySignService.jdBeanHome()
        if (!response.isSuccessful) {
            L.d(TAG, "京东京豆-请求失败")
            return
        }
        val jdBeanHome = response.body()
        jdBeanHome?.run {
            when {
                code == "3" -> {
                    L.d(TAG, "京东商城-京豆Cookie失效")
                }
                data.status == "1" -> {
                    L.d(TAG, "京东商城-京豆签到成功")
                }
                data.status == "2" -> {
                    L.d(TAG, "京东商城-今天已签到，获得奖励")
                }
                else -> {
                    L.d(TAG, "京东商城-京豆: 失败, 原因: 未知 ⚠️")
                }
            }
        }
    }

    suspend fun jdStore() {
        val response = jdDelaySignService.jdStore()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.data?.success == true || body?.data?.bizCode == 811) {
                L.d(TAG, "京东商城-超市签到成功")
                return
            }
        }
        L.d(TAG, "京东商城-超市签到失败 ==> response = $response")
    }

    suspend fun jdTurn() {
        val response = jdDelaySignService.jdTurn()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.data?.lotteryCode == "1") {
                jdTurnSign(body.data.lotteryCode)
                return
            }
        }
    }

    private suspend fun jdTurnSign(code: String) {
        val response =
            jdDelaySignService.jdTurnSign(mapOf("body" to "{\"actId\":\"jgpqtzjhvaoym\",\"appSource\":\"jdhome\",\"lotteryCode\":\"${code}\"}"))
        if (response.isSuccessful) {
            val body = response.body()
            when {
                body?.code == "3" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: Cookie失效‼️")
                }
                body?.errorCode == "T216" -> {
                    L.d(TAG, "抽奖已经结束")
                }
                body?.errorCode == "T215" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 已转过 ⚠️")
                }
                body?.errorCode == "T210" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 无支付密码 ⚠️")
                }
                body?.data?.chances != "0" -> {
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(2000)
                        jdTurnSign(code)
                    }
                }
                else -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 未知 ⚠️")
                }
            }
        }
    }


    suspend fun jdSubsidy() {
        val response = jdDelaySignService.jdSubsidy()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.resultCode == 0 && body.resultData?.code == 0) {
                L.d(TAG, "京东商城-金贴签到成功 ")
            } else if (body?.resultCode == 0 && body.resultData?.data?.thisAmount != 0) {
                L.d(TAG, "京东商城-已经签到")
            } else {
                L.d(TAG, "京东商城-签到失败")
            }
        }
    }

    suspend fun jdGetCash() {
        val response = jdDelaySignService.jdGetCash()
        if (!response.isSuccessful) {
            L.d(TAG, "京东商城-现金签到失败")
            return
        }
        val body = response.body()

        if (body?.data?.success == true && body.data.result != null) {
            L.d(TAG, "京东商城-现金签到成功 明细: ${body.data.result.signCash}现金 \uD83D\uDCB0")
        } else if (body?.data?.bizCode == 201) {
            L.d(TAG, "京东商城-现金: 失败, 原因: 已签过 ⚠️")
        } else if (body?.code == 300) {
            L.d(TAG, "京东商城-现金: 失败, 原因: Cookie失效‼️")
        } else {
            L.d(TAG, "京东商城-现金: 失败, 原因: 未知 ⚠️")
        }
    }

    suspend fun jdShake() {
        val response = jdDelaySignService.jdShake()
        if (!response.isSuccessful) {
            L.d(TAG, "京东商城-摇摇: 失败, 原因: 请求失败")
            return
        }
        val body = response.body()

        if (body?.success == true && body.data.prizeCoupon != null) {
            L.d(TAG, "京东商城-摇一摇签到成功 ")
            if ((body.data.luckyBox?.freeTimes ?: 0) > 0) {
                CoroutineScope(Dispatchers.Default).launch {
                    delay(1000)
                    jdShake()
                }
            }
        } else if (body?.resultCode == "9000005" || body?.resultCode == "8000005") {
            L.d(TAG, "京东商城-摇摇: 失败, 原因: ${body.message}")
        } else {
            L.d(TAG, "京东商城-摇摇: 失败, 原因: 未知 ⚠")
        }
    }

    suspend fun jdSecKilling() {
        val response = jdDelaySignService.jdSecKilling()
        if (!response.isSuccessful) {
            L.d(TAG, "京东秒杀-红包: 失败, 原因: 请求失败")
            return
        }
        val body = response.body()

        if (body?.code == 203 || body?.code == 101 || body?.code == 3) {
            L.d(TAG, "京东秒杀-红包: 失败, 原因: Cookie失效‼️")
        } else if (body?.code == 200 && body.result != null) {
            val projectId = body.result.projectId
            val taskId = body.result.taskId
            L.d(TAG, "京东秒杀-红包查询成功 taskId = $taskId , projectId = $projectId")
            delay(100)
            jdKilling(projectId, taskId)
        } else {
            L.d(TAG, "京东秒杀-红包查询失败 原因: 未知‼️")
        }
    }

    private suspend fun jdKilling(projectId: String, taskId: String) {

        val reqBody = mapOf(
            "functionId" to "doInteractiveAssignment",
            "client" to "wh5",
            "appid" to "SecKill2020",
            "body" to """
                {"encryptProjectId":"$projectId","encryptAssignmentId":"$taskId","completionFlag":true}
            """.trimIndent()
        )

        val response = jdDelaySignService.jdKilling(reqBody)
        if (!response.isSuccessful) {
            L.d(TAG, "京东秒杀-红包: 失败, 原因: 请求失败")
            return
        }
        val body = response.body()

        if (body?.subCode == "0" && body.msg == "success") {
            L.d(TAG, "京东秒杀-红包签到成功")
        } else {
            L.d(TAG, "京东秒杀-红包: 失败")
        }
    }


    /**
     * 京东金贴-双签：code = 1DF13833F7
     * 金融京豆-双签: code = F68B2C3E71 belong = jingdou
     */
    suspend fun jdDoll(title: String, code: String, type: String? = null, belong: String = "") {


        val frontParam = when (code) {
            // 金融双签
            "F68B2C3E71" -> """
                ,"frontParam":{"belong":"$belong"}
            """.trimIndent()
            // 金贴
            "1DF13833F7" -> """
                ,"frontParam":{"channel":"JR","belong":4}
            """.trimIndent()
            else -> ""
        }
        val reqBody = mapOf(
            "reqData" to """
                {"actCode": "$code", "type": "${type ?: 3}" $frontParam }
            """.trimIndent()
        )

        val response = jdDelaySignService.jdDoll(reqBody)

        if (!response.isSuccessful) {
            L.d(TAG, "京东签到-$title 请求失败")
            return
        }

        val body = response.body()

        if (body?.resultCode == 0) {
            L.d(TAG, "$title 签到失败，原因： 未知")
            return
        }

        if (body?.resultData?.code == 200 && body.resultData.data != null) {
            if (code == "1DF13833F7") {
                if (body.resultData.data.businessCode == "000ssq") {
                    val count = (body.resultData.data.businessData.awardListVo ?: emptyList()).sumByDouble { it.count }
                    L.d(TAG, "金融金贴签到成功 获得金贴 $count")
                } else if (body.resultData.data.businessCode == "302ssq") {
                    L.d(TAG, "未在京东app签到")
                }
            } else if (code == "F68B2C3E71") {
                if (body.resultData.data.businessData.businessCode == "000sq") {
                    val count = (body.resultData.data.businessData.awardListVo ?: emptyList()).sumByDouble { it.count }
                    L.d(TAG, "金融双签 获得京豆 ${count.toInt()}")
                } else if (body.resultData.data.businessData.businessCode == "301sq") {
                    L.d(TAG, "未在金融app签到")
                }
            }
        }
    }

    suspend fun jdUserSignPre(title: String, activityId: String, ask: String = "") {
        val reqBody = mapOf(
            "functionId" to "qryH5BabelFloors",
            "client" to "wh5",
            "body" to if (ask.isEmpty()) """
                {"activityId":"$activityId"}
            """.trimIndent() else """
                {"activityId":"$activityId","paginationParam":"2","paginationFlrs":"$ask"}
            """.trimIndent()
        )
        val response = jdDelaySignService.jdUserSignPre(reqBody)
        if (!response.isSuccessful) {
            L.d(TAG, "京东签到-$title : 失败, 原因: 请求失败")
            return
        }

        val body = response.body()

        val floorList = body?.floorList ?: emptyList()
        // TODO 签到数据
        val signInfo = floorList.filter { it.template == "signIn" && it.signInfos.params.contains("\"enActK\"") }.map { it.signInfos }.firstOrNull()
        signInfo?.run {
            if (signStat == "1") {
                L.d(TAG, "京东签到-$title 重复签到")
            } else {
                jdUserSign1(
                    title, """
                    {"params": "$params"} 
                """.trimIndent()
                )
            }
        }
        // TODO 关注店铺

        val turnTableId = floorList.filter { it.boardParams?.turnTableId?.isNotEmpty() ?: false }.map { it.boardParams!!.turnTableId }.firstOrNull()

        val paginationFlrs = body?.paginationFlrs

        if (!turnTableId.isNullOrEmpty()) {
            // 存在关注店铺
            jdUserSign2(title, turnTableId)
        } else if (!paginationFlrs.isNullOrEmpty() && ask.isEmpty()) {
            // 未发现签到数据，尝试带参数查询
            jdUserSignPre(title, activityId, paginationFlrs)
        } else {
            L.d(TAG, "京东签到-$title 失败")
        }

    }

    /**
     * @param body eg: {"params":"{\"enActK\":\"9wKIMMJjQLbQFeZ6KQv0Jd2uoEjYGDUa2jM9qv4aH7mg3o9A4MJ4izn9K7SR3bSRcRRfojzJNzIB\\naiUadRY9Jv6KZBhNOwmCFk0xY4qnbBXxcg+FRoBf1Ned7AMupe1LGmbP5+HKCzc=\",\"isFloatLayer\":false,\"ruleSrv\":\"00898735_60491585_t1\",\"signId\":\"Z1YoCFWVQAgaZs/n4coLNw==\"}"}
     */
    private suspend fun jdUserSign1(title: String, body: String) {
        val reqBody = mapOf(
            "functionId" to "userSign",
            "client" to "wh5",
            "body" to body
        )

        val response = jdDelaySignService.jdUserSign1(reqBody)
        if (!response.isSuccessful) {
            L.d(TAG, "京东签到-$title : 失败, 原因: 请求失败")
            return
        }
        val body = response.body()
        if (body?.code == "0" && (body.signText == "签到成功" || body.signText == "已签到")) {
            L.d(TAG, "京东签到-$title : 成功 | 已签到")
        } else if (body?.code == "3") {
            L.d(TAG, "京东签到-$title : 失败 原因：cookie失效")
        } else {
            L.d(TAG, "京东签到-$title : 失败 原因：${body?.msg}")
        }

    }

    private suspend fun jdUserSign2(title: String, turnTableId: String) {
        val detailUrl = "https://jdjoy.jd.com/api/turncard/channel/detail?turnTableId=${turnTableId}&invokeKey=ztmFUCxcPMNyUq0P"
        jdDelaySignService.jdUserSignDetail(detailUrl)
        val url = "https://jdjoy.jd.com/api/turncard/channel/sign"
        val response = jdDelaySignService.jdUserSign2(
            url, mapOf(
                "invokeKey" to "ztmFUCxcPMNyUq0P",
                "turnTableId" to turnTableId
            )
        )

        if (!response.isSuccessful) {
            L.d(TAG, "京东签到-$title : 失败, 原因: 请求失败")
            return
        }

        val body = response.body()
        if (body?.success == true) {
            L.d(TAG, "京东签到-$title : ${body.data}")
        } else {
            L.d(TAG, "京东签到-$title : 失败")
        }


    }
}