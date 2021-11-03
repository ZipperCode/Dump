package com.zipper.sign.jckd

import com.sun.corba.se.spi.presentation.rmi.StubAdapter.request
import com.zipper.sign.core.*
import com.zipper.sign.core.base.BaseApi
import com.zipper.sign.core.ext.EncodeMode
import com.zipper.sign.core.ext.md5
import com.zipper.sign.core.interceptor.LoggerInterceptor
import com.zipper.sign.core.interceptor.NewInterceptor
import com.zipper.sign.core.util.Base64
import com.zipper.sign.jckd.bean.BrownTaskList
import com.zipper.sign.jckd.bean.MainArticleInfo
import com.zipper.sign.jckd.bean.MainVideoInfo
import io.jsonwebtoken.Header
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import java.io.ByteArrayInputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


fun main() = runBlocking{
    JckdApi().execute()
}

class JckdApi: BaseApi("https://ant.xunsl.com/") {

    private val tag = "晶彩看点"
    private var userId: String = "55513301"

    private var cookie:String = "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl6-Fuaqw3YGwhIyp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdiWqGjJyZrt-uapqGcXY"
    private var cookieId: String = "bfe727b9dd83205335ceaba1f741635a"

    private var zqKeyId: String = "bfe727b9dd83205335ceaba1f741635a"
    private var zqKey: String = "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl6-Fuaqw3YGwhIyp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdiWqGjJyZrt-uapqGcXY"
    private var appVersion = "8.1.1"
    
    private val jckdService: JckdService = create()
    private val youthService: YouthService = create()
    private var readArticleSecond = 0
    private var successReward: Int = 0
    private var withDrawMoney: String = "0.3"
    private var userName = "嘿！哪誰"
    private val taskRewardAction = "task_reward_action"

    suspend fun execute(param: JckdApiParam) {
        userId = param.uid
        zqKeyId = param.zqkeyId
        zqKey = param.zqkey
        cookie = param.token
        cookieId = param.zqkeyId
        userName = param.userName
        withDrawMoney = param.withDrawMoney
        execute()
    }
    
    override suspend fun execute() {
        userInfo()
        signInfo()
        doReadArticleTask()
        doWatchVideoTask()
        doShareArticle()
        doWatchWelfareVideo()
        doWelfareWatchRewardTask()
        checkFinishTask()
        turnTotary()
        withDraw2()
        read60MinuteTask()
        openBox()
        log("任务执行完毕 获得 $successReward 青豆")
    }
    private fun log(msg: String) {
        L.e("$tag-$userId", msg)
    }
    
    suspend fun userInfo() {
        catchExp {
            val userInfo = jckdService.userInfo()
            userInfo.items?.apply {
                userId = uid
                zqKey = zqkey
                zqKeyId = zqkey_id
                readArticleSecond = read_article_second
                L.e(tag, "用户：\t$userId($nickname)")
                L.e(tag, "余额：\t$money_str")
                L.e(tag, "等级：\t$level")
                L.e(tag, "阅读时长：\t$read_article_second")
                L.e(tag, "签到状态：\t$sign_status")
                L.e(tag, "今天总阅读数量：\t$today_read_num")
            }
            log(userInfo.items?.toString() ?: "")
        }
    }

    private suspend fun signInfo() {
        catchExp {
            val taskCenterSignInfo = jckdService.getTaskCenterSignInfo(userId)
            taskCenterSignInfo.items?.run {
                L.e(tag, "用户 $userId 是否签到 = $is_sign 已经连续签到 $total_sign_days ")
                if (!is_sign) {
                    delay(1000)
                    L.e(tag, "执行用户签到")
                    sign("sign_reward_action", "user_sign")
                    delay(3000)
                    L.e(tag, "执行幸运")
                    sign("sign_reward_action", "sign_lucky_reward")
                }
            }
        }
    }

    private suspend fun sign(action: String, param: String, extra: String = "2") {
        val toGetReward2 = jckdService.toGetReward2(action, param, extra)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
            L.e(tag, "【用户签到】获得 ${toGetReward2.items?.score} 青豆")
            if (toGetReward2.items != null) {
                val rewardAction = toGetReward2.items!!.button.reward_action
                val index = toGetReward2.items!!.button.send_reward_action
                delay(30000)
                toDouble(rewardAction, index)
            }
        } else {
            L.e(tag, "【用户签到】失败 ${toGetReward2.message}")
        }
    }

    private suspend fun toDouble(rewardAction: String, index: String, videoId: String = "") {
        catchExp {
            val toGetRewardSecond = jckdService.toGetRewardSecond(rewardAction, index, videoId)
            if (toGetRewardSecond.error_code == "0") {
                val score = toGetRewardSecond.items?.score?.toInt() ?: 0
                successReward += score
                L.e(tag, "【视频广告】获得 $score 青豆")
            } else {
                L.e(tag, "【视频广告】失败：${toGetRewardSecond.message}")
            }
        }
    }

    private suspend fun getReward2(action: String, param: String, extra: String = "") {
        val toGetReward2 = jckdService.toGetReward2(action, param, extra)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
            L.e(tag, "【收取奖励】获得 ${toGetReward2.items?.score} 青豆")
        } else {
            L.e("收取奖励 失败：${toGetReward2.message}")
        }
    }

    private suspend fun rewardTask(tag: String, rewardAction: String, action: String = "task_reward_action") {
        val toGetReward2 = jckdService.toGetReward2(action, rewardAction)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
            L.e(tag, "【$tag】获得 ${toGetReward2.items?.score} 青豆")
        } else {
            L.e("【$tag】 失败：${toGetReward2.message}")
        }
    }

    private suspend fun doReadArticleTask() {
        catchExp {
            L.e(tag, "【阅读文章任务】开始")
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "1" && it.status == 0 }?.run {
                    L.e("【日常-阅读文章任务】开始执行， 已经执行 $title_num/$title_total 次")
                    var count = title_num.toInt()
                    while (count <= title_total.toInt()) {
                        delay(2000)
                        val articleList = getArticleList(0)
                        delay(1000)
                        articleList.forEach {
                            val articleId = it.id
                            val cateId = it.catid
                            readArticle(articleId, cateId, it.signature)
                            count++
                            if (count > title_total.toInt()) {
                                return@forEach
                            }
                        }
                    }
                    var action = reward_action
                    if (action.isEmpty()) {
                        action = "watch_article_reward"
                    }
                    getReward2("task_reward_action", action)
                }
                L.d(tag, "【阅读任务】完成")
            } else {
                L.d(tag, "【阅读任务】失败：$taskCenterTaskInfo")
            }
        }
    }

    private suspend fun doWatchVideoTask() {
        L.e(tag, "【观看视频任务】开始")
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "2" && it.status == 0 }?.run {
                    L.e("【观看视频任务】开始执行， 已经执行 $title_num/$title_total 次")
                    var count = title_num.toInt()
                    while (count <= title_total.toInt()) {
                        delay(2000)
                        val articleList = getVideoList(0)
                        delay(1000)
                        articleList.forEach {
                            delay(30000)
                            readArticleComplete(it.id)
                            count++
                            if (count > title_total.toInt()) {
                                return@forEach
                            }
                        }
                    }
                    var action = reward_action
                    if (action.isEmpty()) {
                        action = "watch_video_reward"
                    }
                    getReward2("task_reward_action", action)
                }
                L.d(tag, "【观看视频任务】完成")
            } else {
                L.d(tag, "【观看视频任务】失败：$taskCenterTaskInfo")
            }
        }
    }

    /**
     * 分享文章
     */
    private suspend fun doShareArticle() {
        L.e(tag, "【分享文章任务】开始")
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "4" }?.run {
                    var action = reward_action
                    if (action.isEmpty()) {
                        action = "first_share_article"
                    }
                    getReward2("task_reward_action", action)
                }
                L.d(tag, "【分享文章任务】完成")
            } else {
                L.d(tag, "【分享文章任务】失败：$taskCenterTaskInfo")
            }
        }
    }

    /**
     * 看福利视频
     */
    private suspend fun doWatchWelfareVideo() {
        L.e(tag, "【看福利视频任务】开始")
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "10" }?.run {
                    var count = title_num.toInt()
                    var times = 0
                    while (count <= title_total.toInt() || times > 12) {
                        try {
                            jckdService.advertVideo()
                            delay(1000)
                            val recordNum = jckdService.recordNum()
                            if (recordNum.error_code == "0") {
                                count++
                            }
                        } catch (e: java.lang.Exception) {
                            L.d(e.message + "")
                        }
                        delaySecond(30)
                        times++
                    }
                    var action = reward_action
                    if (action.isEmpty()) {
                        action = "new_fresh_five_video_reward"
                    }
                    getReward2("task_reward_action", action)
                }
                L.d(tag, "【看福利视频任务】完成")
            } else {
                L.d(tag, "【看福利视频任务】失败：$taskCenterTaskInfo")
            }
        }
    }

    /**
     * 阅读60分钟
     * read_time_sixty_minutes
     */
    private suspend fun read60MinuteTask() {
        catchExp {
            userInfo()
            L.e(tag, "【60分钟阅读】已经阅读 $readArticleSecond 秒")
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "7" && it.status != 2 }?.run {
                    var count = 0
                    while (count < 30) {
                        val articleList = getArticleList(0)
                        delay(1000)
                        articleList.forEach {
                            val articleId = it.id
                            val cateId = it.catid
                            readArticle(articleId, cateId, it.signature)
                            count++
                        }
                    }
                    delaySecond(5)
                    getReward2(taskRewardAction, "read_time_sixty_minutes")
                }
            }
        }
    }

    /**
     * 福利看看赚
     * task_kankan_reward
     */
    private suspend fun doWelfareWatchRewardTask() {
        log("【福利看看赚】 ===开始===")

        doOnceBrownTask()

        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "11" }?.run {
                    delaySecond(10)
                    rewardTask("福利看看赚", "task_kankan_reward")
                }
            }
        }

        L.e(tag, "福利看看赚任务执行完成")
    }

    private suspend fun browseReward() {
        L.e(tag, "====开始执行浏览赚====")
        val taskList = listOf(
            "1182", "4298", "4123", "4124", "4136", "4156", "4417", "4169", "4293", "4316"
        )
        var reward = 0
        for (taskId in taskList) {
            catchExp {
                val browseStart = jckdService.browseStart("browse_read_article_$taskId")
                if (browseStart.success && browseStart.items?.comtele_state == 0) {
                    delaySecond(10)
                    val browseEnd = jckdService.browseEnd("browse_read_article_$taskId")
                    if (browseEnd.success) {
                        successReward += (browseEnd.items?.score ?: 0)
                        reward += (browseEnd.items?.score ?: 0)
                    }
                }
            }
            delaySecond(10)
        }
        L.e(tag, "====浏览赚执行完毕 本次获得 $reward 青豆====")
    }

    private suspend fun adStartReward() {
        L.e(tag, "====开始执行看6篇文章====")
        val taskIdList = mutableListOf<String>()
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                taskCenterTaskInfo.items?.daily?.filter { it.action == "new_box" }?.forEach { task ->
                    if (task.banner_id.isNotEmpty()) {
                        taskIdList.add(task.banner_id)
                    }
                }
            }
        }
        L.e(tag, "将要执行的任务id = $taskIdList")
        var reward = 0
        for (taskId in taskIdList) {
            catchExp {
                val adClickStart = jckdService.adClickStart(taskId)
                delaySecond(3)
                if (adClickStart.success && adClickStart.items?.comtele_state == 0) {
                    val curNum = adClickStart.items?.read_num ?: 6
                    val totalNum = adClickStart.items?.see_num ?: 6
                    for (index in curNum until totalNum) {
                        jckdService.bannerStatus(taskId)
                        delayRandomSecond(3, 8)
                    }
                }
                delaySecond(5)
                val adClickEnd = jckdService.adClickEnd(taskId)
                if (adClickEnd.success) {
                    successReward += (adClickEnd.items?.score ?: 0)
                    reward += (adClickEnd.items?.score ?: 0)
                }
            }
            delaySecond(5)
        }
        L.e(tag, "====看文章6篇执行完毕 本次获得 $reward 青豆====")
    }

    private suspend fun doOnceBrownTask(){
        log("开始执行看看赚浏览任务")
        var reward = 0

        val taskList = catchExp {
            val body = youthService.getTaskBrowse()
            if (body.status == 1) {
                return@catchExp body.data?.list
            }
            return@catchExp null
        } ?: emptyList<BrownTaskList.Task>()
        delaySecond(5)
        taskList.filter { it.status != 2 }.forEach { task ->
            catchExp {
                val taskId = task.banner_id
                log("正在执行 ${task.title} 浏览任务")
                val adClickStart = jckdService.adClickStart(taskId)
                delaySecond(3)
                if (adClickStart.success && adClickStart.items?.comtele_state == 0) {
                    val curNum = adClickStart.items?.read_num ?: 6
                    val totalNum = adClickStart.items?.see_num ?: 6
                    for (index in curNum until totalNum) {
                        jckdService.bannerStatus(taskId)
                        delayRandomSecond(3, 8)
                    }
                }
                delaySecond(5)
                val adClickEnd = jckdService.adClickEnd(taskId)
                if (adClickEnd.success) {
                    successReward += (adClickEnd.items?.score ?: 0)
                    reward += (adClickEnd.items?.score ?: 0)
                }

            }
            delaySecond(10)
        }
        L.e(tag, "====看看赚-浏览 执行完毕 本次获得 $reward 青豆====")

        log("开始收取看看赚宝箱")
        catchExp {
            jckdService.advertVideo(4)
            val body = youthService.getBoxRewardConf()
            body.data?.list?.forEach {
                log("收取宝箱 ${it.id} 可获得 ${it.score}")
                youthService.getBoxReward(it.id)
                successReward += it.score
                delaySecond(10)
                val b = jckdService.gameVideoReward()
                if (b.error_code == "0"){
                    successReward += b.items?.score?.toInt() ?: 0
                }
                delaySecond(2)
            }
        }
        log("收取看看赚宝箱成功")
    }

    private suspend fun openBox() {
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                delaySecond(3)
                getReward2(taskRewardAction, body.box.three.reward_action)
                delaySecond(3)
                toDouble("task_video_reward_double", "task_video_more_reward")
                delaySecond(10)
                getReward2(taskRewardAction, body.box.six.reward_action)
                delaySecond(3)
                toDouble("task_video_reward_double", "task_video_more_reward")
                delaySecond(10)
                getReward2(taskRewardAction, body.box.nine.reward_action)
                delaySecond(3)
                toDouble("task_video_reward_double", "task_video_more_reward")
                delaySecond(10)
            }
        }
        L.e(tag, "盒子开启执行完毕")
    }


    private suspend fun withDraw2() {
        catchExp {
            val d = withDrawMoney.toDouble()
            if (d > 0){
                L.e(tag, "提现:" + jckdService.withDraw2("2", userName, withDrawMoney).message)
            }
        }
    }

    /**
     * cateId = 0 推荐
     * cateId = 20030 热点
     * cateId = 20008 美文
     * cateId = 20007 健康
     * cateId = 20006 娱乐
     * cateId = 20010 搞笑
     */
    private suspend fun getArticleList(cateId: Int = 0, videoCateId: Int = 1453): List<MainArticleInfo> {
        val op = "0"
        val behotTime = "0"
        val oid = "0"
        return jckdService.articleList(cateId, videoCateId, op, behotTime, oid).items ?: emptyList()
    }

    private suspend fun getVideoList(videoCateId: Int = 0): List<MainVideoInfo> {
        return jckdService.videoList(videoCateId).items ?: emptyList()
    }

    private suspend fun checkFinishTask() {
        catchExp {
            val taskCenterTaskInfo = jckdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.new.filter { it.action.isNotEmpty() }.forEach {
                    L.e(tag, "收取新手任务 action = ${it.action}")
                    val toGetReward2 = jckdService.toGetReward2("task_reward_action", it.action)
                    if (toGetReward2.error_code == "0") {
                        successReward += (toGetReward2.items?.score?.toInt() ?: 0)
                        L.e(tag, "【收取新手任务】获得 ${toGetReward2.items?.score} 青豆")
                    } else {
                        L.e("收取新手任务 失败：${toGetReward2.message}")
                    }
                    delay(3000)
                }

                rewardTask("新手任务", "beread_reward_three")
                delaySecond(10)
                rewardTask("搜索领青豆", "task_center_sousuo")
                delaySecond(20)
            }
        }
    }

    /**
     * 转发文章
     */
    private suspend fun shareArticle(articleId: String) {
        val articleShare = jckdService.articleShare(articleId)
        if (articleShare.error_code == "0") {
            L.e(tag, "【文章分享-$articleId】成功")
        } else {
            L.e(tag, "【文章分享-$articleId】失败：${articleShare.message}")
        }
    }

    /**
     * 阅读文章
     */
    private suspend fun readArticle(articleId: String, cateId: String, signature: String) {
        catchExp {
            L.e(tag, "【阅读文章-$articleId】开始 cateId = $cateId signature = $signature")
            jckdService.articleContent(articleId, signature)
            val newArticleDetailResp = jckdService.getNewArticleDetail(articleId, cateId, "0")
            val newArticleDetail = newArticleDetailResp.body()
            if (newArticleDetailResp.isSuccessful && newArticleDetail != null)
                if (newArticleDetail.error_code == "0") {
                    L.e(tag, "【阅读文章-$articleId】等待分享和完成。。。")
                    withContext(Dispatchers.IO) {
                        val share = async {
                            delay(3000)
                            shareArticle(articleId)
                        }
                        val finish = async {
                            delay(30000)
                            readArticleComplete(articleId)
                        }
                        share.await()
                        finish.await()
                    }
                } else {
                    L.e(tag, "【阅读文章-$articleId】失败：${newArticleDetail.message}")
                }
        }
        delay(2000)
    }

    private suspend fun readArticleComplete(articleId: String, readType: String = "0") {
        catchExp {
            val reward = jckdService.articleComplete(articleId, readType)
            if (reward.error_code == "0") {
                val score = reward.items?.read_score ?: 0
                successReward += score
                L.e(tag, "【完成阅读/视频-$articleId】获得 $score 青豆")
            } else {
                L.e(tag, "【完成阅读/视频-$articleId】失败：${reward.message}")
            }
        }
    }

    private suspend fun turnTotary(){
        log("开始执行100次抽奖")
        var remainTurn = 0
        var count = 0
        do{
            catchExp {
                val body = youthService.turnRotary()
                remainTurn = body.data?.remainTurn ?: 0
                successReward += body.data?.score ?: 0
                log("执行第${count}次抽奖 获得 ${body.data?.score ?: 0} 青豆")
            }
            delaySecond(5)
            count++
        }while (remainTurn > 0 && count <= 100)

        for (i in 1..4){
            log("收取抽奖任务奖励 $i")
            catchExp {
                val body = youthService.chestReward()
                val score =  body.data?.get("score") as? String
                successReward += score?.toInt() ?: 0
            }
            delaySecond(5)
        }

        log("100次抽奖完成 总共执行抽奖 $remainTurn 次，循环 $count 次")
    }

    fun getYouthParams(): Map<String, String> {
        return mapOf(
            "keyword_wyq" to "woyaoq.com",
            "access" to "WIFI",
            "app-version" to appVersion,
            "app_version" to appVersion,
            "channel" to "c1025",
            "cookie" to zqKey,
            "cookie_id" to zqKeyId,
            "device_brand" to "Xiaomi",
            "device_id" to "c9a870e2730e8ce3",
            "device_model" to "MI%206",
            "device_platform" to "android",
            "device_type" to "android",
            "inner_version" to "202107191702",
            "mi" to "0",
            "openudid" to "c9a870e2730e8ce3",
            "os_api" to "30",
            "os_version" to "RQ3A.210605.005%20release-keys",
            "phone_network" to "WIFI",
            "phone_sim" to "0",
            "request_time" to secondTimeStr,
            "resolution" to "1080x1920",
            "sim" to "2",
            "sm_device_id" to "2021100220074244c1349baf562f85dc18972d74a0696f01c23b885ac4e4df",
            "subv" to "1.2.2",
            "time" to secondTimeStr,
            "uid" to "55513301",
            "uuid" to "ab9e85faf72a4110b31b6d98a32c8287",
            "version_code" to "800",
            "version_name" to "%E6%99%B6%E5%BD%A9%E7%9C%8B%E7%82%B9",
            "zqkey" to zqKey,
            "zqkey_id" to zqKeyId,
        )
    }

    override fun getInterceptors(): List<Interceptor> {
        return listOf(
            object : NewInterceptor(){
                override fun newRequest(oldRequest: Request): Request {
                    val newRequest = oldRequest.newBuilder()
                    val version = getVersion(oldRequest.url.encodedPathSegments[0])
                    val method = oldRequest.method
                    val extParam: TreeMap<String, String> = getExtraParams(version)
                    val httpUrl = oldRequest.url

                    val urlHead = oldRequest.header("WebApi")
                    if (!urlHead.isNullOrEmpty()) {

                        newRequest
                            .header(
                                "User-Agent",
                                "Mozilla/5.0 (Linux; Android 11; MI 6 Build/RQ3A.210605.005; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/91.0.4472.101 Mobile Safari/537.36"
                            )
                            .header("X-Requested-With", "XMLHttpRequest")

                        if (oldRequest.method.equals("GET", ignoreCase = true)) {
                            val newUrlBuilder = oldRequest.url.newBuilder()
                            extParam.forEach { k, v ->
                                newUrlBuilder.addQueryParameter(k,v)
                            }
                            newRequest.url(newUrlBuilder.build())
                        } else if (oldRequest.method.equals("POST", ignoreCase = true)) {
                            if (oldRequest.body is FormBody) {
                                val newBodyBuilder = FormBody.Builder()
                                val body = oldRequest.body as FormBody
                                for (i in 0 until body.size) {
                                    newBodyBuilder.addEncoded(body.name(i), body.value(i))
                                }

                                extParam.forEach { (k, v) ->
                                    newBodyBuilder.addEncoded(k,v)
                                }
                                newRequest.post(newBodyBuilder.build())
                            }
                        }

                        val refer = oldRequest.header("Referer")
                        if (!refer.isNullOrEmpty()){
                            newRequest.header("Referer","${refer}?${getYouthParams().toUrlParam()}")
                        }

                        return newRequest.build()
                    }



                    if("GET".equals(method, ignoreCase = true)){
                        if (version == 5){
                            for (str2 in httpUrl.queryParameterNames) {
                                httpUrl.queryParameter(str2)?.let { extParam.put(str2, it) }
                            }
                            val sign = sign(extParam, version)
                            val encrypt = encryptParam(extParam)

                            return newRequest
                                .headers(getHeaders())
                                .url("${oldRequest.url.scheme}://${oldRequest.url.host}${oldRequest.url.encodedPath}?p=$encrypt")
                                .build()
                        }else{
                            val newBuilder: HttpUrl.Builder = httpUrl.newBuilder()
                            val hashMap: HashMap<String, String> = HashMap()
                            for (str3 in httpUrl.queryParameterNames) {
                                hashMap[str3] = httpUrl.queryParameter(str3) ?: ""
                            }

                            extParam.forEach { (k, v) ->
                                if (v.isNotEmpty() && !hashMap.containsKey(k)){
                                    newBuilder.addQueryParameter(k, v)
                                }
                            }

                            extParam.putAll(hashMap)
                            sign(extParam, version)
                            if (extParam.containsKey("sign")){
                                newBuilder.addQueryParameter("sign",extParam["sign"])
                            }
                            return newRequest
                                .header("app-type","jckd")
                                .url(newBuilder.build()).build()
                        }
                    }
                    else if ("POST".equals(method, ignoreCase = true)){
                        val body = oldRequest.body
                        if (body is FormBody) {
                            for (i in 0 until body.size) {
                                extParam[body.name(i)] = body.value(i)
                            }
                            sign(extParam, version)
                            if (version == 5){
                                val encrypt = encryptParam(extParam)
                                return  newRequest
                                    .headers(getHeaders())
                                    .post(FormBody.Builder().add("p", encrypt).build())
                                    .build()
                            }

                            val formBuilder = FormBody.Builder()

                            extParam.forEach { (k, v) ->
                                if (k.isNotEmpty() && v.isNotEmpty()){
                                    formBuilder.addEncoded(k,v)
                                }
                            }

                            val sign = extParam["sign"] ?: ""

                            return newRequest.header("Token", sign)
                                .headers(getHeaders())
                                .post(formBuilder.build())
                                .build()
                        }

                        if (body is MultipartBody){
                            val mulBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                            for (i in 0 until body.size) {
                                mulBody.addPart(body.part(i))
                            }

                            sign(extParam, version)

                            extParam.forEach { (k, v) ->
                                if (k.isNotEmpty() && v.isNotEmpty()){
                                    mulBody.addFormDataPart(k,v)
                                }
                            }
                            return newRequest
                                .header("app-type", "jckd")
                                .post(mulBody.build())
                                .build()
                        }

                    }
                    return newRequest.build()
                }
            }
        )
    }

    private fun getCommonParams(): MutableMap<String, String> {
        val treeMap: TreeMap<String, String> = TreeMap<String, String>()
        treeMap["channel"] = "c1025"
        treeMap["sm_device_id"] = "2021100220074244c1349baf562f85dc18972d74a0696f01c23b885ac4e4df"
        treeMap["oaid"] = "DeviceInfoUtils.DEVICE_OAID"
        treeMap["device_id"] = "52256291"
        treeMap["openudid"] = "c9a870e2730e8ce3"
        treeMap["szlm_ddid"] = ""
        treeMap["uuid"] = ""
        treeMap["device_type"] = "android"
        treeMap["device_platform"] = "android"
        treeMap["resolution"] = "1920x1080"
        treeMap["os_version"] = "RQ3A.210605.005 release-keys"
        treeMap["os_api"] = "30"
        treeMap["device_model"] = "MI 6"
        treeMap["device_brand"] = "Xiaomi"
        treeMap["version_name"] = ""
        treeMap["version_code"] = "800"
        treeMap["app-version"] = "zqkd_app"
        treeMap["app_version"] = "zqkd_app"
        treeMap["inner_version"] = "202107191702"
        treeMap["subv"] = "1.2.2"
        treeMap["access"] = "4G"
        treeMap["phone_network"] = "4G"
        treeMap["mi"] = "1"
        treeMap["phone_sim"] = "1"
        treeMap["sim"] = "1"
        treeMap["carrier"] = "%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A"
        return treeMap
    }

    fun getExtraParams(i: Int): TreeMap<String, String>{
        val channel = "c1025"
        val deviceId = "52256291"
        val treeMap = TreeMap<String, String>()
        treeMap["uid"] = userId
        treeMap["language"] = "zh-CN"
        treeMap["os_version"] = "RQ3A.210605.005 release-keys"
        treeMap["os_api"] = "30"
        treeMap["device_model"] = "MI 6"
        treeMap["device_brand"] = "Xiaomi"
        treeMap["device_id"] = "52256291"
        treeMap["fp"] = ""
        treeMap["oaid"] = ""
        treeMap["s_im"] = ""
        treeMap["s_ad"] = "97jvhIkVyrvg=znzBwCtY8GL_TvkgL5oL_yJWJh7yKJEfD"
        treeMap["dpi"] = "480"
        treeMap["app_name"] = "jckd_app"
        treeMap["app_version"] = appVersion
        treeMap["version_code"] = "800"
        treeMap["jssdk_version"] = ""
        treeMap["inner_version"] = "202110181424"
        treeMap["rom_version"] = "RQ3A.210605.005 release-keys"
        treeMap["channel"] = "c1025"
        treeMap["network_type"] = "4G"
        treeMap["carrier"] = "%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A"
        treeMap["ab_version"] = ""
        treeMap["ab_feature"] = ""
        treeMap["ab_client"] = ""
        treeMap["resolution"] = "1920x1080"
        treeMap["sm_device_id"] = "2021100220074244c1349baf562f85dc18972d74a0696f01c23b885ac4e4df"
        treeMap["zqkey_id"] = zqKeyId
        treeMap["zqkey"] = zqKey
        treeMap["mi"] = "1"
        treeMap["mobile_type"] = "1"
        treeMap["net_type"] = "2"
        treeMap["device_platform"] = "android"
        treeMap["szlm_ddid"] = ""
        treeMap["storage"] = "110.25"
        treeMap["memory"] = "5"
        var str4: String = "2"
        if (i == 1 || i == 2) {
            treeMap["device_type"] = "2"
            treeMap["openudid"] = deviceId
            treeMap["channel_code"] = channel
            treeMap["phone_network"] = "WIFI"
            treeMap["phone_code"] = deviceId
            treeMap["client_version"] = appVersion
            treeMap["uuid"] = ""
            treeMap["phone_sim"] = "1"
            treeMap["request_time"] = (System.currentTimeMillis() / 1000.toLong()).toString()
            treeMap["debug"] = "1"
        } else if (i == 3 || i == 4) {
            treeMap["device_type"] = "android"
            treeMap["openudid"] = deviceId
            treeMap["iid"] = ""
            treeMap["access"] = "WIFI"
            treeMap["request_time"] = (System.currentTimeMillis() / 1000.toLong()).toString()
            treeMap["phone_sim"] = "1"
            treeMap["debug"] = "1"
        } else if (i == 5) {
            treeMap["device_type"] = "android"
            treeMap["openudid"] = "03c47748b842844b"
            treeMap["request_time"] = (System.currentTimeMillis() / 1000.toLong()).toString()
        } else if (i != 16) {
            treeMap["device_type"] = "android"
            treeMap["openudid"] = deviceId
            treeMap["app-version"] = appVersion
            treeMap["subv"] = "1.2.2"
            treeMap["request_time"] = (System.currentTimeMillis() / 1000.toLong()).toString()
            treeMap["access"] = "WIFI"
            treeMap["sim"] = "1"
        } else {
            treeMap["device_type"] = "android"
            treeMap["openudid"] = deviceId
            treeMap["ac"] = "1"
            treeMap["version_name"] = "晶彩看点"
            treeMap["ts"] = (System.currentTimeMillis() / 1000.toLong()).toString()
        }
        return treeMap
    }

    private fun getHeaders(): Headers {
        val deviceId = "52256291"
        val hashMap = HashMap<String, String>()
        val string: String = "-"
        hashMap["iid"] = string
        hashMap["device-id"] = deviceId
        hashMap["request_time"] = (System.currentTimeMillis() / 1000.toLong()).toString()
        hashMap["access"] = "4G"
        hashMap["app-version"] = appVersion
        hashMap["device-platform"] = "android"
        hashMap["os_version"] = "RQ3A.210605.005+release-keys"
        hashMap["os-api"] = 26.toString()
        hashMap["device_model"] = "MI+6"
        hashMap["phone-sim"] = "1"
        hashMap["carrier"] = ""
        return hashMap.toHeaders()
    }

    fun getExtraParams(): Map<String, String> {
        val valueOf = (System.currentTimeMillis() / 1000).toString()
        val commonParams = getCommonParams()
        commonParams["uid"] = userId
        commonParams["cookie"] = cookie
        commonParams["cookie_id"] = cookieId
        commonParams["zqkey_id"] = zqKeyId
        commonParams["zqkey"] = zqKey
        commonParams["request_time"] = valueOf
        commonParams["time"] = valueOf
        return commonParams
    }

    companion object{

        const val SIGN_KEY_1 = "jdvylqchJZrfw0o2DgAbsmCGUapF1YChc"
        const val SIGN_KEY_2 = "zWpfzystJLrfw7o3SgGlMmGGPupK2YLhB"

        fun getVersion(str: String): Int {
            return try {
                if (str.matches(Regex("^v\\d+\$"))) {
                    return Regex("\\d+\$").find(str)?.value?.toInt() ?: 1
                }
                1
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                1
            }
        }

        fun sign(extParam: TreeMap<String, String>, version: Int) {
            when (version) {
                1, 2, 3, 4, 5 -> {
                    extParam["sign"] = sign(extParam, SIGN_KEY_1)
                }
                15, 16, 17 -> {
                    extParam["token"] = Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                        .setHeaderParam(JwsHeader.ALGORITHM, "HS512")
                        .setClaims(extParam)
                        .signWith(Keys.hmacShaKeyFor(jwtKey.toByteArray(Charset.forName("UTF-8")))).compact()
                }
                else -> {
                    extParam["token"] = sign(extParam, SIGN_KEY_2)
                }
            }
        }


        fun sign(param: Map<String, String>, key: String): String {
            val stringBuilder = StringBuilder()
            val sortKey = param.keys.sorted()
            val iterator = sortKey.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (!param[next].isNullOrEmpty()) {
                    stringBuilder.append(next).append("=").append(param[next])
                }
            }
            return (stringBuilder.toString() + key).md5(EncodeMode.HEX).toLowerCase()
        }


        fun encryptParam(param: Map<String, String>): String {
            val randomChar = chars.random()
            val stringBuilder = StringBuilder()
            param.forEach { (key, value) ->
                stringBuilder.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)

            val desEncode = encryptDES(parseSignature(Base64.decode(appSign, 0), randomChar), stringBuilder.toString())

            return getDisturbString(desEncode, randomChar)
        }

        fun encryptDES(str: String, str2: String): String {
            return try {
                val bArr = ByteArray(8)
                val instance = MessageDigest.getInstance("MD5")
                instance.update(str.toByteArray())
                val digest = instance.digest()
                for (i in 0..7) {
                    bArr[i] = digest[i]
                }
                val str3 = String(Base64.encode(bArr, 10))
                val ivParameterSpec = IvParameterSpec(str3.substring(0, 8).toByteArray())
                val generateSecret =
                    SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(str.toByteArray()))
                val instance2 = Cipher.getInstance("DES/CBC/PKCS5Padding")
                instance2.init(1, generateSecret, ivParameterSpec)
                val str4 = String(Base64.encode(instance2.doFinal(str2.toByteArray()), 10))
                str3 + str4
            } catch (unused: Exception) {
                ""
            }
        }

        fun getSingInfo(randomChar: Char): String {
            return parseSignature(appSignBase64Decode, randomChar)
        }

        fun parseSignature(bArr: ByteArray, c2: Char): String {
            var str = ""
            return try {
                val substring = String(
                    Base64.encode(
                        (CertificateFactory.getInstance("X.509")
                            .generateCertificate(ByteArrayInputStream(bArr)) as X509Certificate).publicKey
                            .encoded, 10
                    )
                ).substring(9)
                val substring2 = substring.substring(0, substring.length - 5)
                str = substring2.substring(substring2.length - 36)
                str.substring(0, str.length - (c2.toInt() % '\n'.toInt()))
            } catch (e: CertificateException) {
                e.printStackTrace()
                str
            }
        }

        fun decryptReq(str: String): String {
            val newStr = URLDecoder.decode(str, "UTF-8")
            val randomChar = newStr[0]
            val i = (randomChar.toInt() % '\n'.toInt()) % 3
            return decryptDES(getSingInfo(randomChar), newStr.substring(1, newStr.length - i))
        }

        fun decryptDES(str: String, str2: String): String {
            return try {
                val substring = str2.substring(0, 8)
                val decode = Base64.decode(str2.substring(12).toByteArray(), 10)
                val ivParameterSpec = IvParameterSpec(substring.toByteArray())
                val generateSecret =
                    SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(str.toByteArray()))
                val instance = Cipher.getInstance("DES/CBC/PKCS5Padding")
                instance.init(2, generateSecret, ivParameterSpec)
                String(instance.doFinal(decode))
            } catch (unused: Exception) {
                ""
            }
        }

        fun decryptResp(str: String): String {
            val secretKeySpec =
                SecretKeySpec(parseSignature(appSignBase64Decode, 'a').substring(0, 16).toByteArray(), "AES")
            val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
            instance.init(2, secretKeySpec)
            return String(instance.doFinal(Base64.decode(str, 1)))
        }

        private const val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        fun getDisturbString(str: String, c2: Char): String {
            val i: Int = (c2.toInt() % '\n'.toInt()) % 3
            val str2 = StringBuilder(c2.toString() + str)
            for (i2 in 0 until i) {
                str2.append(chars.random())
            }
            return str2.toString()
        }

        private const val appSign = """MIIDWzCCAkOgAwIBAgIEDtoIFTANBgkqhkiG9w0BAQsFADBeMQ8wDQYDVQQGEwYxMDAwMDAxCzAJ
    BgNVBAgTAkJqMQswCQYDVQQHEwJCSjEOMAwGA1UEChMFQ2hpbmExDjAMBgNVBAsTBUNoaW5hMREw
    DwYDVQQDEwh1cHNhbGFyeTAeFw0xNjA1MDkwOTU0NTNaFw00MTA1MDMwOTU0NTNaMF4xDzANBgNV
    BAYTBjEwMDAwMDELMAkGA1UECBMCQmoxCzAJBgNVBAcTAkJKMQ4wDAYDVQQKEwVDaGluYTEOMAwG
    A1UECxMFQ2hpbmExETAPBgNVBAMTCHVwc2FsYXJ5MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB
    CgKCAQEAkhYLHyoNGF9vaG4V9PDuCrelY0GPQxu8RgcceE6KThxsSY6fDeAFoV9upNZhoX8aGbza
    eX5VGXZm1dU2QOX58lr8K251x9cwKMUD4ZvwhP5U05iYsLwuWSIe4AI9iq0id3JZdg+T8PTmCtE/
    JSq0+KlocRnM37eHRIaJLT+zcEccUC4XFw/HRbpyAxfE+AIngDL51adT9lWy+9Df1flEPuj7roU1
    7gCGsggNXCTxLLim/Jc+K5VON9aUJ650YbL7V+j2La6qnI0B9zJa12iyp8jzMEP7+sqGATHm/4Eu
    d814xX9DpOmOg3y82KEJ0tWKbKWqr2aSAOASljXdSqqBhQIDAQABoyEwHzAdBgNVHQ4EFgQU/elG
    oTFvFHlfM24Ub5RLggL5lYMwDQYJKoZIhvcNAQELBQADggEBADFmRWhf62Nt5WOcCNd6E7sXu8dX
    xXBPM2v9KEZLqsbyUtIReT4KMVw7LiJRgIKP0/PZxUWV4boF7ssaL1m7hmuYriyCzvQYGG/omE/e
    +58N+5+f3RMBk6p1EZcbUkNtfzS166aehuaz2ePiheE/iKAYy61QQssw+fJVPEzB1iLjV67RRt+H
    BZjz2hVwI+6++eaR8YigrO//gCNrJxbTwwWrG7kCfJe9mt3uGyAXwouc2FJlbmc+CHV5qqIoAJGm
    AOZ10+hP9AAjaioLT9vy3thXRvMMnk4y/2xvr5Np5dLGZ82xiU+Rt0QP+f2smHKCK/b9r3d8IsGQ
    3MA+sQ+wzL4="""

        val appSignBase64Decode = Base64.decode(appSign, 0)

        const val jwtKey =
            """AAAAB3NzaC1yc2EAAAADAQABAAABAQC1WAth281wjZj5XhGU9Iza5EXzOy5U/AKgGxF14svnCEWrTH6i3lZd+lMTFLvTakGI5l1RJmutFRku6CvDVCEc7dJURVWsrgQTFNBuu0t5WOkoUY0zNa05pejDmBC4w4MscH2OexCrKfHNEYi/FpjBJv1bwjU0luxt/cvsjBjlthgY47I4KNy+T953CpBiYQmkSJZUBzsN2Zz+jEA+CvLEK9BPHBlKcz0GupalgnHHSnS/JoUz8+RTjZr1O2sjSyrcg0LL+vWeCnJN07Uv4jJaTDqc6Ig1Mw+TJrrsARxoA+Frc66Qo7GFxACimuJ1LeCc9iFlMzZNZly3JxYAR019"""


    }


}