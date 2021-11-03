package com.zipper.sign.kugou

import com.zipper.sign.core.*
import com.zipper.sign.core.base.BaseApi
import com.zipper.sign.core.ext.toJsonRequestBody
import com.zipper.sign.kugou.bean.KgTaskProfile
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *  "listen.storagemodule.bss.ktvtracker": "http://bsstrackercdngz.kugou.com/query/download/url/ksong",
 *  "listen.storagemodule.bss_trackerkey": "f5e539a05687aec1a363dd149cf2b16d117d03b4",
 *  "listen.storagemodule.bsstracker_pid": 20075,
 *  "listen.storagemodule.follow_listen.tracker": "https://gateway.kugou.com/genting/v1/music/reqcmd{1123092214}",
 *  "listen.storagemodule.ktv_trackerkey": "c156e5f3823626cdd1b56784169f3e423089bbf6",
 *  "listen.storagemodule.ktvtracker_pid": 20038,
 *  "listen.storagemodule.musiccloud.tracker": "http://bsstrackercdngz.kugou.com/query/download/url/musicclound",
 *  "listen.storagemodule.musiccloud_pid": 20026,
 *  "listen.storagemodule.musiccloud_trackerkey": "ebd1ac3134c880bda6a2194537843caa0162e2e7",
 *
 * </hr>
 *  "listen.switchparam.musichunterkey": "A4D80698F83DD69D273F170F3F131753",
 *
 * </hr>
 * "listen.usersdkparam.appid": 1005,
 * "listen.usersdkparam.appkey": "OIlwieks28dk2k092lksi2UIkp",
 * "listen.usersdkparam.apprsa": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIAG7QOELSYoIJvTFJhMpe1s/gbjDJX51HBNnEl5HXqTW6lQ7LC8jr9fWZTwusknp+sVGzwd40MwP6U5yDE27M/X1+UR4tvOGOqp94TJtQ1EPnWGWXngpeIW5GxoQGao1rmYWAu6oi1z9XkChrsUdC6DJE5E221wf/4WLFxwAtRQIDAQAB",
 * "listen.usersdkparam.gamecenter.zego_appid": "1333368999",
 * "listen.usersdkparam.gamecenter.zego_appkey": "a8379cbbb48e82d8099fbc739e9365a4de844faa7ba60ba6edbf0577407b85e8",
 */
class KgTaskApi(private val configBean: ConfigBean) : BaseApi("https://escp.kugou.com/") {

    companion object {
        const val TAG: String = "酷狗任务"
        const val SIGN_1: String = "NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt"
        const val SIGN_2: String = "OIlwieks28dk2k092lksi2UIkp"
        const val APP_ID: String = "1005"
        const val MID: String = "19866310614991365975980225640804152227"
        const val UUID: String = "532544371bb026cc684489e57eedbe9c"
        const val CLIENT_VER: String = "10829"
        const val AGENT_8823 = "Android10-AndroidPhone-10829-14-0-mobileCallProtocol-wifi"
        const val CHARS = "0123456789abcdef"

        fun signature(param: Map<String, String>, jsonBody: String = ""): String{
            return signature(SIGN_1,param, jsonBody)
        }

        fun signature(key: String, param: Map<String, String>, jsonBody: String = ""): String{
            val result: StringBuilder = StringBuilder()
            val sortedKeys = param.keys.sorted()
            val iterator = sortedKeys.iterator()
            val strBuilder = StringBuilder(key)
            while (iterator.hasNext()){
                val paramKey = iterator.next()
                val paramValue = param[paramKey]
                strBuilder.append(paramKey).append("=").append(paramValue)
            }
            val paramStr = strBuilder.append(jsonBody).append(key).toString()

            val md5Data =  paramStr.toByteArray().md5()

            for (data in md5Data){
                result.append(byte2String(data))
            }
            return result.toString()
        }

        private fun byte2String(byte: Byte) : String{
            var tmp = byte.toInt()
            if (tmp < 0){
                tmp = byte + 256
            }
            val a = tmp / 16
            val b = tmp % 16
            return "${CHARS[a]}${CHARS[b]}"
        }
    }

    private val kgTaskService: KgTaskService = retrofit.create(KgTaskService::class.java)

    private var userId: String = configBean.uid
    private var token: String = configBean.token
    private var h5Token: String = configBean.h5Token

    private val userAgent: String
        get() = if(configBean.userAgent.isEmpty()) AGENT_8823 else configBean.userAgent

    private var isInit: Boolean = true

    private val closedTaskList: MutableList<Int> = mutableListOf()

    /**
     * 是否执行仅一次的任务
     */
    private var canExecuteOnceTask: Boolean = false

    private var rewardCoin: Int = 0

    private val commonParam: MutableMap<String, String>
        get() = mutableMapOf(
            "dfid" to if (configBean.dfId.isEmpty()) "4Wwvsj2nDISI017CmR4L886I" else configBean.dfId,
            "appid" to APP_ID ,
            "mid" to if (configBean.mid.isEmpty()) MID else configBean.mid,
            "clientver" to CLIENT_VER,
            "uuid" to if (configBean.uuid.isEmpty()) UUID else configBean.uuid,
            "from" to "client",
            "userid" to userId,
            "token" to token
        )

    override suspend fun execute() {
        if (!isInit) {
            L.d("must be init")
            return
        }
        signState()
        profile()
        info()
        list()

        fixTimeSubmit()
    }

    private fun log(msg: String){
        L.e("$TAG--$userId", msg)
    }
    /**
     * 获取签到天数
     */
    private suspend fun signState() {
        log(" 查询账号[${userId}]的签到状态")
        val param = commonParam
        param["clienttime"] = secondTimeStr
        param["signature"] = signature(SIGN_2, param, "")
        catchExp {
            val body = kgTaskService.signState(param)
            if (!(body.errcode == 0 && body.status == 1)) {
                log("查询签到状态失败 code = ${body.errcode} msg = ${body.error}")
                return@catchExp
            }
            val signDate = body.data.list.firstOrNull { it.today == 1 }
            if (signDate == null) {
                log( "未查询到用户${userId}的当天签到状态")
                return@catchExp
            }
            val rewardString =
                if (signDate.type == "coin") "${signDate.award_coins} 狗狗币" else "VIP ${signDate.award_vips} 天"
            log("当前签到第${signDate}天，获得 $rewardString")
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val date1 = SimpleDateFormat("yyyyMMdd").format(Date())
            log("当前时间 $date1 上一次签到时间 ${signDate.code}")
            if (signDate.code == date1 && !date1.isNullOrEmpty()) {
                log("当前时间与上一次签到时间不同，执行签到")
                signOn(date1)
            }
        }

    }

    /**
     * 签到
     */
    private suspend fun signOn(signDate: String, doubleCode: String = "") {
        log("开始执行用户签到,是否double = ${doubleCode.isNotEmpty()}")
        val firstJson = """{"code":"$signDate"}"""
        val secondJson = """{"code":"$signDate","double_code":"$doubleCode"}"""
        val param = commonParam
        param["srcappid"] = "2919"
        param["clienttime"] = millsTimeStr
        val jsonBody = if (doubleCode.isEmpty()) {
            param["signature"] = signature(param, firstJson)
            firstJson
        } else {
            param["signature"] = signature(param, secondJson)
            secondJson
        }

        val body = kgTaskService.signOn(param, jsonBody.toJsonRequestBody())
        if (body.status == 1 && body.errcode == 0) {
            var reward = 0
            body.data.list.forEach {
                reward += it.award_coins
            }
            if (!body.is_double) {
                log("第一次签到成功获得 $reward 狗狗币 等待5秒")
                rewardCoin += reward
                delaySecond(5)
                if (!body.data.double_code.isNullOrEmpty()) {
                    signOn(signDate, body.data.double_code)
                }
            } else {
                rewardCoin += reward
                log("看视频第二次签到成功 $reward 狗狗币 等待5秒")
                delaySecond(5)
            }
        } else {
            log("执行签到失败 ${body.error} 延迟3秒")
            delaySecond(3)
        }
    }

    suspend fun profile() {
        log("开始获取任务列表")
        val param = commonParam
        param["clienttime"] = secondTimeStr
        param["tkick"] = "1"
        param["signature"] = signature(SIGN_2, param)
        val body = kgTaskService.profile(param)
        if (!(body.status == 1 && body.errcode == 0)) {
            log("任务列表获取失败 ${body.errcode}_${body.error}")
            return
        }
        // 过滤定时任务
        body.data.task.filter { it.taskid != 1105 && it.taskid != 1108 }.forEach {
            when (it.taskid) {
                9 -> flushVideoRedPack(it)
                else -> {
                    if (it.limit_type == 2) {
                        if (canExecuteOnceTask) {
                            onceTaskSubmit(it)
                        } else {
                            L.d(TAG, "如果需要执行一次性任务，将 canExecuteOnceTask 设置为true")
                        }
                    } else {
                        sampleTaskSubmit(it)
                    }
                }
            }
            delaySecond(5)
        }
    }

    /**
     * 刷视频红包
     */
    private suspend fun flushVideoRedPack(taskProfileTask: KgTaskProfile.Task) {
        val taskId = 9
        catchExp {
            val isOpen = taskProfileTask.open
            val awardList = taskProfileTask._award_list
            val maxDoneCount = taskProfileTask.max_done_count
            log("开始执行刷视频红包任务，是否启用：$isOpen 最大执行次数：$maxDoneCount")
            if (awardList.isNotEmpty()) {
                // taskid = 9 看视频
                awardList.filter { award -> !award.done }.forEachIndexed { index, award ->
                    log("执行第 $index 次 执行后延迟20秒")
                    submit(taskId)
                    delaySecond(20)
                }
            }
        }
        log("执行刷视频红包任务-完成")
    }

    /**
     * 简单任务
     * limitType = 1
     */
    private suspend fun sampleTaskSubmit(taskProfileTask: KgTaskProfile.Task) {
        val taskId = taskProfileTask.taskid
        val maxDoneCount = taskProfileTask.max_done_count
        catchExp {
            log( "开始执行简单任务 taskId: $taskId 最多可执行${maxDoneCount}次")
            var times = 1
            var doneCount = stateList(taskId)
            if (doneCount == -2) {
                delay(5000)
                while (times <= maxDoneCount && !closedTaskList.contains(taskId)) {
                    log("简单任务执行第 $times 次 执行完延迟30秒")
                    submit(taskId)
                    delaySecond(30)
                    times++
                }
                return@catchExp
            }
            while (doneCount != -1
                && doneCount <= maxDoneCount
                && times <= maxDoneCount
                && !closedTaskList.contains(taskId)
            ) {
                log("简单任务执行第 $times 次 执行完延迟30秒")
                submit(taskId)
                times++
                delay(5000)
                doneCount = stateList(taskId)
                delay(25000)
            }
        }
        log("简单任务执行完毕")
    }

    /**
     * 像绑定微信，实名认证，消息提醒这一类的
     * limitType = 2
     */
    private suspend fun onceTaskSubmit(taskProfileTask: KgTaskProfile.Task) {
        val taskId = taskProfileTask.taskid
        val maxDoneCount = taskProfileTask.max_done_count
        catchExp {
            log("一天一次任务 taskId = $taskId maxDoneCount = $maxDoneCount")
            submit(taskId)
        }
    }

    private suspend fun fixTimeSubmit() {
        val taskId = 1105
        log("开始执行定时收取狗狗币")
        catchExp {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            submit(taskId)
            var nextTime = stateList(taskId, true)
            if (nextTime == -2) {
                log("h5Token 失效， 无法获取接口, 使用固定延迟")
                fitDelaySubmit(taskId, 0)
                return@catchExp
            }
            var doneCount = stateList(taskId)
            delay(3000)
            while (doneCount <= 60) {
                nextTime = stateList(taskId, true)
                val next = try {
                    val instant = Instant.ofEpochSecond(nextTime.toLong())
                    dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
                } catch (e: Exception) {
                    e.printStackTrace()
                    "null"
                }
                log("下一次执行的时间为 $next 已经执行 $doneCount 次")
                delay(5000)
                if (next == "null") {
                    fitDelaySubmit(taskId, doneCount)
                    return@catchExp
                }
                // 到下一个时间继续执行
                val delayTime = nextTime - Instant.now().epochSecond
                L.d(TAG, "delayTime = $delayTime")
                if (delayTime < 10 * 60) {
                    delay(delayTime * 1000)
                    submit(taskId)
                }
                delay(5000)
                doneCount = stateList(taskId)
            }
        }
    }

    private suspend fun fitDelaySubmit(taskId: Int, initDoneCount: Int = 0) {
        for (i in initDoneCount..60) {
            val s = (10 + (Math.random() * 3).toInt()).toLong()
            delay(s * 60 * 1000)
            submit(taskId)
        }
    }

    /**
     * 查询任务状态
     * @param taskId 任务id
     * @result 完成任务次数
     */
    private suspend fun stateList(taskId: Int = 0, needRetNextTime: Boolean = false): Int {
        if (h5Token.isEmpty()) return -2
        val reqBody = """{}"""
        val params = commonParam
        params.putAll(mapOf(
            "srcappid" to "2919",
            "dfid" to "-",
            "spec" to "15",
            "channel" to "musicsymbol10829",
            "clienttime" to "${Date().time}",
        ))

        params["signature"] = signature(SIGN_1, params, reqBody)

        val result = catchExp {
            val body = kgTaskService.stateList(params, reqBody.toJsonRequestBody())
            if (body.status == 1 && body.errcode == 0) {
                val taskStateList = body.data.list.filter { it.taskid == taskId }.toList()
                if (needRetNextTime) {
                    return@catchExp taskStateList.firstOrNull()?.next_award_time ?: -1
                }
                if (taskStateList.isNotEmpty()) {
                    return@catchExp taskStateList.first().done_count
                }
                return@catchExp -1
            } else if (h5Token.isEmpty() && body.errcode == 1002) {
                return@catchExp -2
            } else {
                L.d("【stateList】code = ${body.errcode} msg = ${body.error}")
            }
            return@catchExp -1
        }
        return result ?: -1
    }

    /**
     * taskId = 9 刷视频红包
     * taskId 6 分享歌曲好友
     * taskId 11 分享视频好友
     * taskId 20 发布动态
     * taskId 25 评论
     * taskId 22 语音直播5分钟
     * taskId 31 观看视频10分钟
     * taskId 24 竖屏mv10分钟
     * taskId 23 有声读物10分钟
     * taskId 29 绑定微信
     * taskId 28 绑定手机
     * taskId 26 实名认证
     * taskId 27 投稿歌单
     * taskId 1101 听歌10分钟
     * taskId 1102 听歌30分钟
     * taskId 1103 听歌60分钟
     * taskId 1104 听歌120分钟
     * taskId 1105 每十分钟领取一次金币
     * taskId 1106 打开奖励提醒
     * taskId 1107 抽奖活动
     * taskId 1110 新人礼包
     */
    suspend fun submit(taskId: Int = 1110, doubleCode: String = "") {
        log("【submit】开始submit任务 [$taskId] 是否第二次提交 [${doubleCode.isNotEmpty()}]")
        val firstJson = "{\"taskid\":$taskId}"
        val secondJson = "{\"taskid\": $taskId,\"double_code\":\"$doubleCode\"}"
        val param = commonParam
        param["clienttime"] = secondTimeStr
        param["dfid"] = "-"
        val reqJson = if (doubleCode.isEmpty()) {
            firstJson
        } else {
            secondJson
        }
        param["signature"] = signature(SIGN_2, param, reqJson)
        catchExp {
            val body = kgTaskService.submit(param, reqJson.toJsonRequestBody())
            if (body.status == 1 && body.errcode == 0) {
                if (body.is_double == null && body.is_double == false) {
                    rewardCoin += body.data.awards.coins
                    L.e(TAG, "【submit】第一次任务执行完成 获得 ${body.data.awards.coins} 狗狗币，等待5秒")
                    delaySecond(5)
                    val doubleCode1 = body.data.double_code
                    if (!doubleCode1.isNullOrEmpty()) {
                        submit(taskId, doubleCode1)
                    }
                } else {
                    L.e(TAG, "【submit】第二次任务-看视频 成功 获得 ${body.data.awards.coins} 狗狗币，等待5秒")
                    delaySecond(5)
                }
            } else if (body.status == 0 && body.errcode == 40001) {
                L.e(TAG, "【submit】任务被关闭了, 任务[$taskId]加入黑名单，等待3秒")
                if (!closedTaskList.contains(taskId)) {
                    closedTaskList.add(taskId)
                }
            }else{
                log("【submit】 失败 ： ${body.error}")
            }
            delaySecond(5)
        }
    }



    /**
     * 兑换商品列表
     */
    suspend fun list() {
        val param = commonParam
        param.putAll(mapOf(
            "token" to h5Token,
            "dfid" to "-",
            "clienttime" to secondTimeStr
        ))
        param["signature"] = signature(SIGN_2, param, "")
        catchExp {
            val response = kgTaskService.list(param)
            if (!response.isSuccessful) {
                L.d(TAG, "【info】request error http code = ${response.code()} ")
                return@catchExp
            }
            val body = response.body()
            if (body?.status == 1 && body.errcode == 0) {
                val giftTypeList = body.data.list
                giftTypeList.forEach {
                    L.d(TAG, it.classname)
                    it.gift_list.filter { gift -> gift.can_excharge }.forEach { gift ->
                        L.d(TAG, "商品：${gift.name} 需要花费：${gift.coins}狗狗币，还剩${gift.total_num - gift.remain_num}件")
                    }
                }
            } else {
                // 20006 签名错误
                L.d(TAG, "【info】 code = ${body?.errcode} msg = ${body?.error}")
            }
        }
    }

    /**
     * 任务账号信息 狗狗币数量
     */
    private suspend fun info() {
        L.e("用户 $userId 查询狗狗币数量")
        if (h5Token.isEmpty()) {
            L.e("用户 $userId 查询狗狗币数量失败：h5Token 为空")
            return
        }
        val param = commonParam
        param["spec"] = "15"
        param["token"] = h5Token
        param["clienttime"] = secondTimeStr
        param["signature"] = signature(SIGN_2, param, "")

        catchExp {
            val body = kgTaskService.info(param)
            if (body.status == 1 && body.errcode == 0) {
                L.e(TAG, "用户：${body.data.base.nickname} 是否新用户： ${body.data.state.is_new_user}")
                L.e(TAG, "总的狗狗币 ${body.data.account.total_coins} 相当于人民币 ${body.data.account.total_coins / 10000.0}元")
            } else {
                // 20006 签名错误
                L.e(TAG, "【info】 code = ${body.errcode} msg = ${body.error}")
            }
        }
    }

    override fun getInterceptors(): List<Interceptor> {
        return listOf(
//            LoggerInterceptor(),
            object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()

                    val newRequest = request.newBuilder()

                    val agent = request.header("User-Agent")
                    if (agent.isNullOrEmpty()){
                        newRequest.header("User-Agent", userAgent)
                    }
                    val fake = request.header("KG-FAKE")
                    if(fake.isNullOrEmpty()){
                        newRequest.header("KG-FAKE", userId)
                    }

                    return chain.proceed(newRequest.build())
                }
            }
        )
    }
}