package com.zipper.api.kgmodule

import android.os.Build
import com.zipper.api.kgmodule.bean.*
import com.zipper.api.module.BaseApi
import com.zipper.api.module.MLog
import com.zipper.api.module.LoggerInterceptor
import com.zipper.api.module.StringUtil
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
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
class KgTaskApi : BaseApi("https://escp.kugou.com/") {

    companion object {
        const val TAG: String = "KgTaskApi"

        const val SIGN_1: String = "NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt"
        const val SIGN_2: String = "OIlwieks28dk2k092lksi2UIkp"

        const val APP_ID: String = "1005"
        const val MID: String = "19866310614991365975980225640804152227"
        const val UUID: String = "532544371bb026cc684489e57eedbe9c"
        const val CLIENT_VER: String = "10829"

        const val UUID_TEST: String = "532544371bb026cc684489e57eedbe9c"
        const val USER_ID_TEST: String = "1882565136"
        const val TOKEN_TEST: String = "808f24298f4c16d5fc94034065381d1864df85b05b5083bf56a249eaafd73504"

        const val UUID_8823: String = "916c7ea0b5e6da7be8c1cc6ae9939569"
        const val USER_ID_8823: String = "128004709"
        const val TOKEN_8823: String = "61c0488e45e44e421ec0aa0210b34e201c2e00171ea18f5744c9440f1494c34b"
        const val COOKIE_8823: String = "WNJTia5EFtpWyQH%2B2AXv3K9nEKqaR3PrO%2FXBmkCj6RbK06r56JxUPji4jDr4VqnM2RfRiSJIOIJna2HJ5BQCCA%3D%3D"

    }

    private val kgTaskService: KgTaskService = create()

    private var token: String = ""

    private var h5Token: String = ""

    private var userId: String = ""

    private var isInit: Boolean = true

    private val closedTaskList: MutableList<Int> = mutableListOf()

    /**
     * 是否执行仅一次的任务
     */
    private var canExecuteOnceTask: Boolean = false

    private val commonParam: MutableMap<String, String> get() = mutableMapOf(
        "dfid" to "-",
        "appid" to APP_ID,
        "mid" to MID,
        "clientver" to CLIENT_VER,
        "uuid" to UUID,
        "from" to "client",
        "userid" to userId,
        "token" to token
    )

    fun testInit(){
        this.userId = "1882565136"
        this.token = "808f24298f4c16d5fc94034065381d1864df85b05b5083bf56a249eaafd73504"
        this.h5Token = "h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57"
        isInit = true
    }

    fun init(userId: String, token: String){
        this.userId = userId
        this.token = token
        this.h5Token = ""
        isInit = true
    }

    override fun getInterceptors(): List<Interceptor> {
        return listOf(
            LoggerInterceptor()
        )
    }

    override suspend fun execute() {
        if(!isInit){
            MLog.d("must be init")
            return
        }
        signState()
        profile()
        info()
        list()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fitTimeSubmit()
        }
    }

    suspend fun profile() {
//        val param = mutableMapOf(
//            "dfid" to "-",
//            "appid" to APP_ID,
//            "mid" to MID,
//            "clientver" to CLIENT_VER,
//            "from" to "client",
//            "clienttime" to "${Date().time / 1000}", // 1630849591
//            "uuid" to UUID,
//            "userid" to userId,
//            "tkick" to "1",
//            "token" to token
//        )
        val param = commonParam
        param["clienttime"] = "${Date().time / 1000}"
        param["tkick"] = "1"
        param["signature"] = StringUtil.signature(SIGN_2, param,"")

        catchExp {
            val response = kgTaskService.profile(param)

            if(!response.isSuccessful){
                MLog.d(TAG,"【profile】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp
            }

            val body = response.body()

            if(!(body?.errcode == 0 && body.status == 1)){
                MLog.d(TAG, "【signState】 code = ${body?.errcode} msg = ${body?.error}")
                return@catchExp
            }
            val taskList = body.data.task
            // 过滤定时任务
            taskList.filter { it.taskid == 1105 }.forEach {
                when (it.taskid) {
                    9 -> flushVideoRedPack(it)
                    else -> {
                        if (it.limit_type == 2) {
                            if(canExecuteOnceTask){
                                onceTaskSubmit(it)
                            }else{
                                MLog.d(TAG, "如果需要执行一次性任务，将 canExecuteOnceTask 设置为true")
                            }
                        } else {
                            sampleTaskSubmit(it)
                        }
                    }
                }
                delay(5000)
            }
        }
    }

    /**
     * 刷视频红包
     */
    private suspend fun flushVideoRedPack(taskProfileTask: KgTaskProfile.Task) {
        val taskId = 9
        catchExp {
            val isOpen = taskProfileTask.open
            val awardType = taskProfileTask.award_type
            val awardList = taskProfileTask._award_list
            val maxDoneCount = taskProfileTask.max_done_count
            MLog.d(TAG, "【flushVideoRedPack】isOpen = $isOpen awardType = $awardType maxDoneCount = $maxDoneCount")
            if (awardList.isNotEmpty()) {
                // taskid = 9 看视频
                awardList.filter { award -> !award.done }.forEach { _ ->
                    submit(taskId)
                    delay(5000)
                }
            }
        }
    }

    /**
     * 简单任务
     * limitType = 1
     */
    private suspend fun sampleTaskSubmit(taskProfileTask: KgTaskProfile.Task) {
        val taskId = taskProfileTask.taskid
        val maxDoneCount = taskProfileTask.max_done_count
        catchExp {
            MLog.d(TAG, "【sampleTaskSubmit】taskId = $taskId maxDoneCount = $maxDoneCount")
            var times = 1
            var doneCount = stateList(taskId)
            while (doneCount != -1 && doneCount != maxDoneCount && times <= maxDoneCount && !closedTaskList.contains(taskId)){
                submit(taskId)
                times++
                doneCount = stateList(taskId)
                delay(5000)
            }
        }
    }

    /**
     * 像绑定微信，实名认证，消息提醒这一类的
     * limitType = 2
     */
    private suspend fun onceTaskSubmit(taskProfileTask: KgTaskProfile.Task) {
        val taskId = taskProfileTask.taskid
        val maxDoneCount = taskProfileTask.max_done_count
        catchExp {
            MLog.d(TAG, "【onceTaskSubmit】taskId = $taskId maxDoneCount = $maxDoneCount")
            submit(taskId)
        }
    }

    private suspend fun fitTimeSubmit() {
        val taskId = 1105
        MLog.d(TAG, "【fixTimeSubmit】taskId = $taskId")
        catchExp {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val resp = submit(taskId)
            
            if(resp == null){
                MLog.d(TAG,"【fitTimeSubmit】submit 接口返回 null 不再继续执行了")
                return@catchExp
            }

            val doneCount = resp.data.state.done_count
            val maxCountDone = resp.data.state.max_done_count
            val nextAwardTime = resp.data.state.next_award_time + 60
            val randomNetAwardTime = nextAwardTime + getRandomNum()
            MLog.d(TAG,"")

            if(doneCount <= maxCountDone){
                MLog.d(TAG,"【定时收币】已执行${doneCount}次，最大可执行${maxCountDone}次，预计下次可执行时间为${dateFormat.format(Date(nextAwardTime * 1000L))}")
                // 调度任务执行
            }
        }
    }

    private suspend fun fitDelaySubmit(taskId: Int, initDoneCount: Int = 0){
        for (i in initDoneCount..81){
            val s = (10 + (Math.random() * 3).toInt()).toLong()
            delay(s * 60 * 1000)
            submit(taskId)
        }
    }

    private fun getRandomNum(start: Int = 0, end: Int = 100): Int{
        return (start + (Math.random() * end).toInt())
    }

    /**
     * 查询任务状态
     * @param taskId 任务id
     * @result 完成任务次数
     */
    suspend fun stateList(taskId: Int = 0, needRetNextTime: Boolean = false): Int {
        if(h5Token.isEmpty()) return -2
        val reqBody = """{}"""
        val userId = "1882565136"
//        val params = mutableMapOf(
//            "srcappid" to "2919",
//            "clientver" to "10829",
//            "clienttime" to  "${Date().time}",  //"1630849669476",
//            "mid" to MID,
//            "uuid" to UUID,
//            "dfid" to "-",
//            "userid" to userId,
//            "token" to h5Token,
//            "appid" to APP_ID,
//            "from" to "client",
//            "spec" to "15",
//            "channel" to "musicsymbol10829"
//        )
        val params = commonParam
        params["srcappid"] = "2919"
        params["token"] = h5Token
        params["spec"] = "15"
        params["channel"] = "musicsymbol10829"
        params["signature"] = StringUtil.signature(SIGN_1, params, reqBody)

        val result = catchExp {
            val response = kgTaskService.stateList(params, reqBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
            if (!response.isSuccessful) {
                MLog.d(TAG,"【submit】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp -1
            }

            val body = response.body()
            if (body?.status == 1 && body.errcode == 0) {
                val taskStateList = body.data.list.filter { it.taskid == taskId }.toList()
                if(needRetNextTime){
                    return@catchExp taskStateList.firstOrNull()?.next_award_time ?: -1
                }
                if (taskStateList.isNotEmpty()) {
                    return@catchExp taskStateList.first().done_count
                }
                return@catchExp -1
            }else if(h5Token.isEmpty() && body?.errcode == 1002){
                return@catchExp -2
            } else{
                MLog.d(TAG, "【stateList】code = ${body?.errcode} msg = ${body?.error}")
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
    suspend fun submit(taskId: Int = 1110, doubleCode: String = "") : KgTaskSubmit?{
        val firstJson = "{\"taskid\":$taskId}"
        val secondJson = "{\"taskid\": $taskId,\"double_code\":\"$doubleCode\"}"

//        val param = mutableMapOf(
//            "dfid" to "-",
//            "appid" to APP_ID,
//            "mid" to MID,
//            "clientver" to "10829",
//            "from" to "client",
//            "clienttime" to  "${Date().time / 1000}",    //"1630850016",
//            "uuid" to UUID,
//            "userid" to userId,
//            "token" to token
//        )

        val param = commonParam
        param["clienttime"] = "${Date().time / 1000}"
        val reqJson = if (doubleCode.isEmpty()) { firstJson } else { secondJson }
        param["signature"] = StringUtil.signature(SIGN_2, param, reqJson)
        MLog.d(TAG, "【submit】taskId = $taskId isDouble = ${doubleCode.isNotEmpty()}")

        return catchExp {
            val response = kgTaskService.submit(param, reqJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()) )
            if (!response.isSuccessful) {
                MLog.d(TAG,"【submit】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp null
            }
            val body = response.body()

            if (body?.status == 1 && body.errcode == 0) {
                if (body.is_double == false) {
                    MLog.d("$taskId 第一次任务完成")
                    val doubleCode1 = body.data.double_code
                    if (!doubleCode1.isNullOrEmpty()) {
                        delay(5000)
                        submit(taskId, doubleCode1)
                    }
                } else {
                    MLog.d("$taskId 看视频第二次任务成功")
                }
            }else if(body?.status == 0 && body.errcode == 40001){
                if(!closedTaskList.contains(taskId)){
                    closedTaskList.add(taskId)
                }
            }
            return@catchExp body
        }
    }

    /**
     * 获取签到天数
     */
    suspend fun signState() {
        val param = commonParam
        param["clienttime"] = "${Date().time / 1000}"
        param["signature"] = StringUtil.signature(SIGN_2, param, "")
        catchExp {
            val response = kgTaskService.signState(param)

            if (!response.isSuccessful) {
                MLog.d(TAG,"【signState】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp
            }

            val body = response.body()

            if(!(body?.errcode == 0 && body.status == 1)){
                MLog.d(TAG, "【signState】 code = ${body?.errcode} msg = ${body?.error}")
                return@catchExp
            }

            var signDay = 1
            val signDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            val signDate = kotlin.runCatching { signDateFormat.format(Date()) }.getOrNull()

            val currentDateFormat = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
            val currentDate = kotlin.runCatching { currentDateFormat.format(Date()) }.getOrNull()

            body.data.list.forEach {
                if (it.today == 1) {
                    MLog.d(TAG, "当前时间 $currentDate 上一次签到时间 ${it.code}")
                    MLog.d(TAG, "当前签到第${signDay}天，获得 ${if (it.type == "coin") "${it.award_coins} 狗狗币" else "VIP ${it.award_vips} 天"}")
                    if(it.code != currentDate && !signDate.isNullOrEmpty()){
                        MLog.d(TAG, "当前时间与上一次签到时间不同，执行签到")
                        signOn(signDate)
                    }
                    return@forEach
                } else {
                    signDay++
                }
            }
        }
    }

    /**
     * 兑换商品列表
     */
    suspend fun list(){
//        val param = mutableMapOf(
//            "userid" to userId,
//            "token" to h5Token,
//            "appid" to APP_ID,
//            "from" to "client",
//            "dfid" to "-",
//            "mid" to MID,
//            "clientver" to "10829",
//            "clienttime" to "${Date().time / 1000}",  // 1630849673
//            "uuid" to UUID
//        )

        val param = commonParam
        param["token"] = h5Token
        param["clienttime"] = "${Date().time / 1000}"
        param["signature"] = StringUtil.signature(SIGN_2, param, "")

        catchExp {
            val response = kgTaskService.list(param)
            if(!response.isSuccessful){
                MLog.d(TAG, "【info】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp
            }
            val body = response.body()
            if(!(body?.status == 1 && body.errcode == 0)){
                // 20006 签名错误
                MLog.d(TAG, "【info】 code = ${body?.errcode} msg = ${body?.error}")
                return@catchExp
            }

            val giftTypeList = body.data.list
            giftTypeList.forEach {
                it.gift_list.filter { gift -> gift.can_excharge }.forEach { gift ->
                    MLog.d(TAG, "商品：${gift.name} 需要花费：${gift.coins}狗狗币，还剩${gift.total_num - gift.remain_num}件")
                }
                MLog.d(TAG, "------------------------------------------------------------------------------------------")
            }
        }
    }

    /**
     * 任务账号信息 狗狗币数量
     */
    suspend fun info(){
//        val param = mutableMapOf(
//            "userid" to userId,
//            "token" to h5Token,
//            "appid" to APP_ID,
//            "from" to "client",
//            "spec" to "15",
//            "dfid" to "-",
//            "mid" to MID,
//            "clientver" to "10829",
//            "clienttime" to "${Date().time / 1000}",  // 1630849663
//            "uuid" to UUID
//        )

        val param = commonParam

        param["token"] = h5Token
        param["spec"] = "15"
        param["clienttime"] = "${Date().time / 1000}"
        param["signature"] = StringUtil.signature(SIGN_2, param, "")

        catchExp {
            val response = kgTaskService.info(param)
            if(!response.isSuccessful){
                MLog.d(TAG, "【info】接口访问失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp
            }

            val body = response.body()
            if (body?.status == 1 && body.errcode == 0) {
                MLog.d(TAG, "用户：${body.data.base.nickname} 是否新用户： ${body.data.state.is_new_user}")
                MLog.d(TAG, "总的狗狗币 ${body.data.account.total_coins} 相当于人民币 ${body.data.account.total_coins / 10000.0}元")
            }else{
                // 20006 签名错误
                MLog.d(TAG, "【info】 code = ${body?.errcode} msg = ${body?.error}")
            }
        }
    }

    /**
     * 签到
     */
    suspend fun signOn(signDate: String , doubleCode: String = "") {
        val firstJson = """{"code":"$signDate"}"""
        val secondJson = """{"code":"$signDate","double_code":"$doubleCode"}"""

        val param = commonParam

        param["srcappid"] = "2919"
        param["clienttime"] = "${Date().time}"

//        val param = mutableMapOf(
//            "srcappid" to "2919",
//            "clientver" to "10829",
//            "clienttime" to "${Date().time}", // 1630851621967
//            "mid" to MID,
//            "uuid" to UUID,
//            "dfid" to "-",
//            "userid" to userId,
//            "token" to token,
//            "appid" to APP_ID,
//            "from" to "client"
//        )

        val jsonBody = if (doubleCode.isEmpty()) {
            param["signature"] = StringUtil.signature(param, firstJson)
            firstJson
        } else {
            param["signature"] = StringUtil.signature(param, secondJson)
            secondJson
        }

        catchExp {
            val response = kgTaskService.signOn(param, jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))

            if(!response.isSuccessful){
                MLog.d(TAG, "【signOn】接口请求失败 http code = ${response.code()} msg = ${response.message()}")
                return@catchExp
            }
            val body = response.body()

            if(!(body?.status == 1 && body.errcode == 0)){
                MLog.d("body error = ${body?.error}")
                return@catchExp
            }

            if (!body.is_double) {
                MLog.d("第一次签到成功")
                val doubleCode1 = body.data.double_code
                if (!doubleCode1.isNullOrEmpty()) {
                    signOn(signDate, doubleCode1)
                }
            } else {
                MLog.d("看视频第二次签到成功")
            }
        }
    }

}