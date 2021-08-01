package com.jd.api

import android.content.Context
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.utils.MemoryManager
import com.zipper.auto.api.api.jd.JdBaseApi
import com.zipper.auto.api.api.jd.bean.JoyyTokenResult
import com.zipper.auto.api.api.jd.bean.NeckLaceHomeResult
import com.zipper.auto.api.api.jd.bean.Result
import com.zipper.core.utils.FileUtil
import com.zipper.core.utils.L
import com.zipper.core.utils.StringUtil
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*

class JdNecklace : JdBaseApi() {

    override fun domain(): String = "bh.m.jd.com"

    var joyytoken: String =
        "MDFJSXVrRTAxMQ==.eH9HXHJ8fENScHF/RBV0ejk+Xh8xAi0bO3hlQ0dwZXgLWTt4Nws=.81e79f82"

    val userName = "jd_59739e7a7a296"

    /**
     * 任务列表
     */
    private var taskResult: Result? = null

    override suspend fun execute(context: Context) {
        val scriptPath = "jd/ZooFaker_Necklace.js"
        val fileName = StringUtil.getPathFileName(scriptPath)
        try {
            context.assets.open(scriptPath).use {
                val zooFakerScript = FileUtil.readString(it)
                val v8 = V8.createV8Runtime(javaClass.simpleName)
                val memoryManager = MemoryManager(v8)
                v8.executeScript(zooFakerScript)
                cacheScriptMemManager[fileName] = memoryManager
                cacheScript[fileName] = v8
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }

//        getJoyyToken()
        delay(2000)
        home()
        delay(1000)
        if(taskResult == null){
            return
        }

        doTask()

    }

    suspend fun getJoyyToken() {
        try {
            val json = post(
                "https://bh.m.jd.com/gettoken", RequestBody.create(
                    "application/text".toMediaTypeOrNull(),
                    "content={\"appname\":\"50082\",\"whwswswws\":\"\",\"jdkey\":\"\",\"body\":{\"platform\":\"1\"}}"
                )
            )
            val data: JoyyTokenResult = convert(json)
            joyytoken = "${data.joyytoken}"
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
                startTask(task.id)
            }
        }
    }


    suspend fun startTask(
        taskId: Int,
        functionId: String = "necklace_startTask",
        itemId: String = ""
    ) {
        val body = mapOf(
            "taskId" to taskId,
            "currentDate" to taskResult!!.lastRequestTime.replace(":", "%3A")
        )

        if (functionId == "necklace_startTask") {
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
                val scriptRes = runScriptMethod("ZooFaker_Necklace", "utils",
                    "decipherJoyToken",
                    appid + joyytoken,
                    appid
                )
                L.d("运行脚本结果 [ZooFaker_Necklace][utils][decipherJoyToken] = $scriptRes")


                val encrypt_id = ""

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

        }

    }

}