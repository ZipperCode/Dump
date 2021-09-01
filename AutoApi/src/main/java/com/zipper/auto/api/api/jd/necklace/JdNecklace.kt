package com.zipper.auto.api.api.jd.necklace

import android.content.Context
import android.util.Base64
import com.zipper.auto.api.api.jd.JdBaseApi
import com.zipper.auto.api.api.jd.bean.JoyyTokenResult
import com.zipper.auto.api.api.jd.bean.NeckLaceHomeResult
import com.zipper.auto.api.api.jd.bean.Result
import com.zipper.auto.api.script.ZooFakerNecklaceScript
import com.zipper.core.utils.L
import com.zipper.core.utils.StringUtil
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*

class JdNecklace : JdBaseApi("") {

    override fun domain(): String = "bh.m.jd.com"

    var joyytoken: String =
        "MDFJSXVrRTAxMQ==.eH9HXHJ8fENScHF/RBV0ejk+Xh8xAi0bO3hlQ0dwZXgLWTt4Nws=.81e79f82"

    val userName = "jd_59739e7a7a296"

    /**
     * 任务列表
     */
    private var taskResult: Result? = null

    private var joyytoken_count: Int = 0

    private val zooFakerNecklaceScript: ZooFakerNecklaceScript = ZooFakerNecklaceScript()

    override suspend fun execute(context: Context) {
        zooFakerNecklaceScript.init(context)
        getJoyyToken()
        delay(2000)
        home()
        delay(1000)
        if (taskResult == null) {
            return
        }
        doTask()

    }

    suspend fun getJoyyToken() {
        try {
            if (joyytoken_count <= 18) {
                return
            }
            joyytoken_count = 0
            val json = post(
                "https://bh.m.jd.com/gettoken", RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    "content={\"appname\" to\"50082\",\"whwswswws\":\"\",\"jdkey\":\"\",\"body\":{\"platform\":\"1\"}}"
                )
            )
            val data: JoyyTokenResult = convert(json)
            joyytoken = "${data.joyytoken}"
            joyytoken_count++

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun home() {
        val functionId = "necklace_homePage"
        val url =
            "https://api.m.jd.com/api?functionId=${functionId}&appid=coupon-necklace&loginType=2&client=coupon-necklace&t=${System.currentTimeMillis()}"
        val headers = mapOf<String, String>(
            "Host" to "api.m.jd.com",
            "accept" to "application/json, text/plain, */*",
            "content-type" to "application/x-www-form-urlencoded",
            "origin" to "https://h5.m.jd.com",
            "accept-language" to "zh-cn",
            "User-Agent" to userAgent,
            "referer" to "https://h5.m.jd.com/",
            "cookie" to "joyytoken=${"50082$joyytoken"};pt_key=AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0;pt_pin=jd_59739e7a7a296;"
        )

        try {
            val json = post(
                url, RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    "body=${StringUtil.escape(gson.toJson(emptyMap<String, String>()))}"
                ), headers
            )
            val result: NeckLaceHomeResult = gson.fromJson(json, NeckLaceHomeResult::class.java)
            if (result.rtnCode == 0 && result.data.bizCode == 0) {
                taskResult = result.data.result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun doTask() {
        for (task in taskResult?.taskConfigVos ?: emptyList()) {
            if (task.taskStage == 0) {
                print("【${task.taskName}】 任务未领取,开始领取此任务")
                necklaceStartTask1(task.id)
            }
        }
    }

    suspend fun necklaceStartTask(
        taskId: Int,
        functionId: String = "necklace_startTask",
        itemId: String = ""
    ) {
        val body = mutableMapOf(
            "taskId" to taskId,
            "currentDate" to taskResult!!.lastRequestTime.replace(":", "%3A")
        )

        val url =
            "https://api.m.jd.com/api?functionId=${functionId}&appid=coupon-necklace&loginType=2&client=coupon-necklace&t=${System.currentTimeMillis()}"
        val headers = mapOf<String, String>(
            "Host" to "api.m.jd.com",
            "accept" to "application/json, text/plain, */*",
            "content-type" to "application/x-www-form-urlencoded",
            "origin" to "https://h5.m.jd.com",
            "accept-language" to "zh-cn",
            "User-Agent" to userAgent,
            "referer" to "https://h5.m.jd.com/",
            "cookie" to "joyytoken=${"50082$joyytoken"};pt_key=AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0;pt_pin=jd_59739e7a7a296;"
        )

        if (functionId == "necklace_startTask") {
            getJoyyToken()
            val id = taskId
            val action = "startTask"
            val appid = "50082"
            var random = Math.floor(1e+6 * Math.random()).toString().padEnd(6, '8')
            val riskData = when (action) {
                "startTask" -> "taskId=$taskId"
                "chargeScores" -> "bubleId=$taskId"
                else -> ""
            }
            val sendDataStr =
                "pin=${userName}&random=${random}${if (riskData.isNotEmpty()) "&$riskData" else ""}"
            val time = Date().time

            try {

                val encrypt_id = (zooFakerNecklaceScript.decipherJoyToken(
                    joyytoken,
                    appid
                )["encrypt_id"] as String).split(",")
                L.d("运行脚本结果 [ZooFaker_Necklace][utils][decipherJoyToken] = $encrypt_id")
                val nonce_str = zooFakerNecklaceScript.getRandomWord(10)
                val key = zooFakerNecklaceScript.getKey(encrypt_id[2], nonce_str, time.toString())
                var str1 =
                    "${sendDataStr}&token=${joyytoken}&time=${time}&nonce_str=${nonce_str}&key=${key}&is_trust=1"
                str1 = zooFakerNecklaceScript.sha1(str1)
                val outstr = arrayListOf(
                    time,
                    "1$nonce_str$joyytoken",
                    encrypt_id[2] + "," + encrypt_id[3],
                    str1,
                    zooFakerNecklaceScript.getCrcCode(str1),
                    "C"
                )
                val uuid = UUID.randomUUID().toString()
                val data = mapOf(
                    "tm" to emptyArray<Unit>(),
                    "tnm" to emptyArray<Unit>(),
                    "grn" to joyytoken_count,
                    "ss" to zooFakerNecklaceScript.getTouchSession(),
                    "wed" to "ttttt",
                    "wea" to "ffttttua",
                    "pdn" to arrayListOf(
                        7,
                        (Math.floor(Math.random() * 1e8) % 180) + 1,
                        6,
                        11,
                        1,
                        5
                    ),
                    "jj" to 1,
                    "cs" to zooFakerNecklaceScript.hexMD5("Object.P.<computed>=&HTMLDocument.Ut.<computed>=https://storage.360buyimg.com/babel/00750963/1942873/production/dev/main.e5d1c436.js"),
                    // cs:"4d8853ea4e4f10d8bde7071dd7fb0ed4",
                    "np" to "iPhone",
                    "t" to time,
                    "jk" to uuid,
                    "fpb" to "",
                    "nv" to "Apple Computer, Inc.",
                    "nav" to "167741",
                    "scr" to arrayListOf(896, 414),
                    "ro" to arrayListOf(
                        "iPhone12,1",
                        "iOS",
                        "14.3",
                        "10.0.8",
                        "167741",
                        "uuid",
                        "a"
                    ),
                    "ioa" to "fffffftt",
                    "aj" to "u",
                    "ci" to "w3.1.0",
                    "cf_v" to "01",
                    "bd" to sendDataStr,
                    "mj" to arrayListOf(1, 0, 0),
                    "blog" to "a",
                    "msg" to ""
                )
                val jsonData = gson.toJson(data)
                val xorResult = zooFakerNecklaceScript.xorEncrypt(jsonData, key)

                val resData = Base64.encodeToString(xorResult.toByteArray(), Base64.DEFAULT)
                outstr.add(resData)
                val crcResult = zooFakerNecklaceScript.getCrcCode(resData)
                outstr.add(crcResult)
                val riskDataList = try {
                    arrayOf(
                        riskData.split("=")[0], riskData.split("=")[1]
                    )
                }catch (e: java.lang.Exception){
                    e.printStackTrace()
                    emptyArray<String>()
                }
                val finalResult = mapOf(
                    "extraData" to mapOf(
                        "log" to outstr.reduce {n, a -> "${n}~$a"},
                        "sceneid" to  "DDhomePageh5"
                    ),
                    if(riskDataList.size == 2) "" to "" else riskDataList[0] to riskDataList[1],
                    "random" to random
                )
                val json = post(
                    url, RequestBody.create(
                        "application/text".toMediaTypeOrNull(),
                        "body=${StringUtil.escape(gson.toJson(finalResult))}"
                    ), headers
                )
                println()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }else{
            if(itemId != ""){
                body["itemId"] = itemId
            }
            try {
                val json = post(
                    url, RequestBody.create(
                        "application/text".toMediaTypeOrNull(),
                        "body=${StringUtil.escape(gson.toJson(body))}"
                    ), headers
                )
                println()
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun necklaceStartTask1(
        taskId: Int,
        functionId: String = "necklace_startTask",
        itemId: String = ""
    ) {
        val body = mutableMapOf(
            "taskId" to taskId,
            "currentDate" to taskResult!!.lastRequestTime.replace(":", "%3A")
        )

        val url =
            "https://api.m.jd.com/api?functionId=${functionId}&appid=coupon-necklace&loginType=2&client=coupon-necklace&t=${System.currentTimeMillis()}"
        val headers = mapOf<String, String>(
            "Host" to "api.m.jd.com",
            "accept" to "application/json, text/plain, */*",
            "content-type" to "application/x-www-form-urlencoded",
            "origin" to "https://h5.m.jd.com",
            "accept-language" to "zh-cn",
            "User-Agent" to userAgent,
            "referer" to "https://h5.m.jd.com/",
            "cookie" to "joyytoken=${"50082$joyytoken"};pt_key=AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0;pt_pin=jd_59739e7a7a296;"
        )

        if (functionId == "necklace_startTask") {
            getJoyyToken()
            val action = "startTask"
            val appid = "50082"
            val body = zooFakerNecklaceScript.get_risk_result(joyytoken, joyytoken_count,action,taskId,userName,UUID.randomUUID().toString())

            val json = post(
                url, RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    "body=${StringUtil.escape(gson.toJson(body))}"
                ), headers
            )
            println()
        }else{
            if(itemId != ""){
                body["itemId"] = itemId
            }
            try {
                val json = post(
                    url, RequestBody.create(
                        "application/text".toMediaTypeOrNull(),
                        "body=${StringUtil.escape(gson.toJson(body))}"
                    ), headers
                )
                println()
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    @Deprecated("")
    suspend fun taskPostUrl(functionId: String, body: Map<String,*>){
        val url =
            "https://api.m.jd.com/api?functionId=${functionId}&appid=coupon-necklace&loginType=2&client=coupon-necklace&t=${System.currentTimeMillis()}"
        val headers = mapOf<String, String>(
            "Host" to "api.m.jd.com",
            "accept" to "application/json, text/plain, */*",
            "content-type" to "application/x-www-form-urlencoded",
            "origin" to "https://h5.m.jd.com",
            "accept-language" to "zh-cn",
            "User-Agent" to userAgent,
            "referer" to "https://h5.m.jd.com/",
            "cookie" to "joyytoken=${"50082$joyytoken"};pt_key=AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0;pt_pin=jd_59739e7a7a296;"
        )

        try {
            val json = post(
                url, RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    "body=${StringUtil.escape(gson.toJson(body))}"
                ), headers
            )
            println()
//            val result: NeckLaceHomeResult = gson.fromJson(json, NeckLaceHomeResult::class.java)
//            if (result.rtnCode == 0 && result.data.bizCode == 0) {
//                taskResult = result.data.result
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decipherJoyToken() {

    }

}