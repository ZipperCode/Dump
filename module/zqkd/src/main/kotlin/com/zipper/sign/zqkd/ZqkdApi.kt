package com.zipper.sign.zqkd

import com.zipper.sign.core.L
import com.zipper.sign.core.base.BaseApi
import com.zipper.sign.core.delayRandomSecond
import com.zipper.sign.core.delaySecond
import com.zipper.sign.core.ext.EncodeMode
import com.zipper.sign.core.ext.md5
import com.zipper.sign.core.interceptor.LoggerInterceptor
import com.zipper.sign.core.toUrlParam
import com.zipper.sign.core.util.Base64
import com.zipper.sign.zqkd.bean.BrownTaskList
import com.zipper.sign.zqkd.bean.MainArticleInfo
import com.zipper.sign.zqkd.bean.MainVideoInfo
import io.jsonwebtoken.Header
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kotlinx.coroutines.*
import okhttp3.*
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

class ZqkdApi : BaseApi("https://kandian.wkandian.com/") {

    private val tag: String = "中青看点"
    private var mUid: String = "59370586"
    private var mZqkeyId: String = "6cc8d0db8c0e8529c1f38d5e7d6cf631"
    private var mZqkey: String =
        "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdhbOEfHaZr7m6apqGcXY"
    private var mToken: String =
        "MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIejl7CFsWWwp4lthaKp4LDPyGl9onqkj3ZqYJa8Y898najWsJupZLDdgW2FsnbgrqnMapqGcXY"
    private var mTokenId: String = "6cc8d0db8c0e8529c1f38d5e7d6cf631"
    private var appVersion: String = "3.7.10"
    private val zqkdService: ZqkdService = create()
    private val youthService: YouthService = create()
    private var successReward: Int = 0
    private var readArticleSecond = 0
    private var withDrawMoney: String = "0.3"
    private var userName = "嘿！哪誰"
    private val taskRewardAction = "task_reward_action"

    override suspend fun execute() {
        userInfo()
        signInfo()
        doReadArticleTask()
        doWatchVideoTask()
        doShareArticle()
        doWatchWelfareVideo()
        read5MinuteTask()
        doWolkStepsTask()
        doWelfareWatchRewardTask()
        checkFinishTask()
        turnTotary()
        withDraw2()
        read60MinuteTask()
        openBox()
        log("任务执行完毕 获得 $successReward 青豆")
    }

    private fun log(msg: String) {
        L.e("中青看点-$mUid", msg)
    }

    suspend fun execute(param: ZqkdApiParam) {
        mUid = param.uid
        mZqkeyId = param.zqkeyId
        mZqkey = param.zqkey
        mToken = param.token
        mTokenId = param.zqkeyId
        userName = param.userName
        withDrawMoney = param.withDrawMoney
        execute()
    }


    private suspend fun userInfo() {
        catchExp {
            val userInfo = zqkdService.userInfo()
            userInfo.items?.apply {
                mUid = uid
                mToken = token
                mTokenId = token_id
                mZqkey = zqkey
                mZqkeyId = zqkey_id
                readArticleSecond = read_article_second
               log("【用户信息】用户：\t$mUid($nickname)")
               log("【用户信息】余额：\t$money_str")
               log("【用户信息】等级：\t$level")
               log("【用户信息】阅读时长：\t$read_article_second")
               log("【用户信息】签到状态：\t$sign_status")
               log("【用户信息】今天总阅读数量：\t$today_read_num")
            }
        }
        delaySecond(2)
    }

    private suspend fun signInfo() {
        catchExp {
            val taskCenterSignInfo = zqkdService.getTaskCenterSignInfo(mUid)
            taskCenterSignInfo.items?.run {
               log("【签到状态】 $mUid 是否签到 = $is_sign 已经连续签到 $total_sign_days ")
                if (!is_sign) {
                    delay(1000)
                   log("执行用户签到")
                    sign("sign_reward_action", "user_sign")
                    delay(3000)
                   log("执行幸运")
                    sign("sign_reward_action", "sign_lucky_reward")
                }
            }
        }
    }

    private suspend fun sign(action: String, param: String, extra: String = "2") {
        val toGetReward2 = zqkdService.toGetReward2(action, param, extra)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
           log("【用户签到】获得 ${toGetReward2.items?.score} 青豆")
            if (toGetReward2.items != null) {
                val rewardAction = toGetReward2.items!!.button.reward_action
                val index = toGetReward2.items!!.button.send_reward_action
                delay(30000)
                toDouble(rewardAction, index)
            }
        } else {
           log("【用户签到】失败 ${toGetReward2.message}")
        }
    }

    private suspend fun toDouble(rewardAction: String, index: String, videoId: String = "") {
        catchExp {
            val toGetRewardSecond = zqkdService.toGetRewardSecond(rewardAction, index, videoId)
            if (toGetRewardSecond.error_code == "0") {
                val score = toGetRewardSecond.items?.score?.toInt() ?: 0
                successReward += score
               log("【视频广告】获得 $score 青豆")
            } else {
               log("【视频广告】失败：${toGetRewardSecond.message}")
            }
        }
    }

    private suspend fun getReward2(action: String, param: String, extra: String = "") {
        val toGetReward2 = zqkdService.toGetReward2(action, param, extra)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
           log("【收取奖励】获得 ${toGetReward2.items?.score} 青豆")
        } else {
            L.e("【收取奖励】 失败：${toGetReward2.message}")
        }
    }

    private suspend fun rewardTask(tag: String, rewardAction: String, action: String = "task_reward_action") {
        val toGetReward2 = zqkdService.toGetReward2(action, rewardAction)
        if (toGetReward2.error_code == "0") {
            successReward += (toGetReward2.items?.score?.toInt() ?: 0)
           log("【$tag】获得 ${toGetReward2.items?.score} 青豆")
        } else {
            L.e("【$tag】 失败：${toGetReward2.message}")
        }
    }

    private suspend fun doReadArticleTask() {
        log("【阅读文章任务】===开始===")
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
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
                L.d(tag, "【阅读文章任务】完成")
            } else {
                L.d(tag, "【阅读文章任务】失败：$taskCenterTaskInfo")
            }
        }
        log("【阅读文章任务】===结束===")
    }

    private suspend fun doWatchVideoTask() {
       log("【观看视频任务】===开始===")
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
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
        log("【观看视频任务】===结束===")
    }

    /**
     * 分享文章
     */
    private suspend fun doShareArticle() {
       log("【分享文章任务】开始")
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
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
       log("【看福利视频任务】===开始===")
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "10" }?.run {
                    var count = title_num.toInt()
                    log("【看福利视频任务】已经执行 $count / $title_total 次")
                    var times = 0
                    while (count <= title_total.toInt() || times > 6) {
                        try {
                            zqkdService.advertVideo()
                            delay(1000)
                            val recordNum = zqkdService.recordNum()
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
        log("【看福利视频任务】===结束===")
    }


    /**
     * 阅读5分钟
     */
    private suspend fun read5MinuteTask() {
        catchExp {
            userInfo()
           log("【5分钟阅读】已经阅读 $readArticleSecond 秒")
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                delaySecond(2)
                if (readArticleSecond >= 60) {
                    getReward2(taskRewardAction, "read_time_two_minutes")
                }
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
            log("【60分钟阅读】已经阅读 $readArticleSecond 秒")
            // 30 分钟就直接结束
            if (readArticleSecond >= 60 * 30) {
                getReward2(taskRewardAction, "read_time_sixty_minutes")
            } else {
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

    /**
     * 福利看看赚
     * task_kankan_reward
     */
    private suspend fun doWelfareWatchRewardTask() {
        browseReward()
        adStartReward()
        doOnceBrownTask()

        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.daily.firstOrNull { it.id == "11" }?.run {
                    delaySecond(10)
                    rewardTask("福利看看赚", "task_kankan_reward")
                }
            }
        }

       log("福利看看赚任务执行完成")
    }

    private suspend fun browseReward() {
       log("====开始执行浏览赚====")
        val taskList = listOf(
            "1182", "4298", "4123", "4124", "4136", "4156", "4417", "4169", "4293", "4316"
        )
        var reward = 0
        for (taskId in taskList) {
            catchExp {
                val browseStart = zqkdService.browseStart("browse_read_article_$taskId")
                if (browseStart.success && browseStart.items?.comtele_state == 0) {
                    delaySecond(10)
                    val browseEnd = zqkdService.browseEnd("browse_read_article_$taskId")
                    if (browseEnd.success) {
                        successReward += (browseEnd.items?.score ?: 0)
                        reward += (browseEnd.items?.score ?: 0)
                    }
                }
            }
            delaySecond(10)
        }
       log("====浏览赚执行完毕 本次获得 $reward 青豆====")
    }

    private suspend fun adStartReward() {
       log("====开始执行看6篇文章====")
        val taskIdList = mutableListOf<String>()
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                taskCenterTaskInfo.items?.daily?.filter { it.action == "new_box" }?.forEach { task ->
                    if (task.banner_id.isNotEmpty()) {
                        taskIdList.add(task.banner_id)
                    }
                }
            }
        }
       log("将要执行的任务id = $taskIdList")
        var reward = 0
        for (taskId in taskIdList) {
            catchExp {
                val adClickStart = zqkdService.adClickStart(taskId)
                delaySecond(3)
                if (adClickStart.success && adClickStart.items?.comtele_state == 0) {
                    val curNum = adClickStart.items?.read_num ?: 6
                    val totalNum = adClickStart.items?.see_num ?: 6
                    for (index in curNum until totalNum) {
                        zqkdService.bannerStatus(taskId)
                        delayRandomSecond(3, 8)
                    }
                }
                delaySecond(5)
                val adClickEnd = zqkdService.adClickEnd(taskId)
                if (adClickEnd.success) {
                    successReward += (adClickEnd.items?.score ?: 0)
                    reward += (adClickEnd.items?.score ?: 0)
                }
            }
            delaySecond(5)
        }
       log("====看文章6篇执行完毕 本次获得 $reward 青豆====")
    }

    private suspend fun doOnceBrownTask(){
        ;log("开始执行看看赚浏览任务")
        var reward = 0

        val taskList = catchExp {
            val body = youthService.getTaskBrowse()
            if (body.status == 1) {
                return@catchExp body.data?.list
            }
            return@catchExp null
        } ?: emptyList<BrownTaskList.Task>()
        delaySecond(5)

        for (task in taskList) {
            catchExp {
                val taskId = task.banner_id
                log("正在执行 ${task.title} 浏览任务")
                val browseStart = zqkdService.browseStart("browse_read_article_$taskId")
                if (browseStart.success && browseStart.items?.comtele_state == 0) {
                    delaySecond(5)
                    val curNum = browseStart.items?.read_num ?: 6
                    val totalNum = browseStart.items?.see_num ?: 6
                    for (index in curNum until totalNum) {
                        log("正在执行 ${task.title} 浏览第 $index 篇文章")
                        zqkdService.bannerStatus(taskId)
                        delayRandomSecond(3, 10)
                    }
                    delaySecond(10)
                    val browseEnd = zqkdService.browseEnd("browse_read_article_$taskId")
                    if (browseEnd.success) {
                        successReward += (browseEnd.items?.score ?: 0)
                        reward += (browseEnd.items?.score ?: 0)
                    }
                }
            }
            delaySecond(10)
        }
       log("====看看赚-浏览 执行完毕 本次获得 $reward 青豆====")

        log("开始收取看看赚宝箱")
        catchExp {
            zqkdService.advertVideo(4)
            val body = youthService.getBoxRewardConf()
            body.data?.list?.forEach {
                log("收取宝箱 ${it.id} 可获得 ${it.score}")
                youthService.getBoxReward(it.id)
                successReward += it.score
                delaySecond(10)
                val b = zqkdService.gameVideoReward()
                if (b.error_code == "0"){
                    successReward += b.items?.score?.toInt() ?: 0
                }
                delaySecond(2)
            }
        }
        log("收取看看赚宝箱成功")
    }

    /**
     * 同步步数
     * walk_task_reward
     */
    private suspend fun doWolkStepsTask() {
        catchExp {
            getReward2(taskRewardAction, "walk_task_reward")
        }
    }

    private suspend fun openBox() {
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
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
       log("盒子开启执行完毕")
    }


    private suspend fun withDraw2() {
        catchExp {
            val d = withDrawMoney.toDouble()
            if (d > 0){
               log("提现:" + zqkdService.withDraw2("2", userName, withDrawMoney).message)
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
        return zqkdService.articleList(cateId, videoCateId, op, behotTime, oid).items ?: emptyList()
    }

    private suspend fun getVideoList(videoCateId: Int = 0): List<MainVideoInfo> {
        return zqkdService.videoList(videoCateId).items ?: emptyList()
    }

    private suspend fun checkFinishTask() {
        catchExp {
            val taskCenterTaskInfo = zqkdService.getTaskCenterTaskInfo()
            val body = taskCenterTaskInfo.items
            if (taskCenterTaskInfo.error_code == "0" && body != null) {
                body.new.filter { it.action.isNotEmpty() }.forEach {
                   log("收取新手任务 action = ${it.action}")
                    val toGetReward2 = zqkdService.toGetReward2("task_reward_action", it.action)
                    if (toGetReward2.error_code == "0") {
                        successReward += (toGetReward2.items?.score?.toInt() ?: 0)
                       log("【收取新手任务】获得 ${toGetReward2.items?.score} 青豆")
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
        val articleShare = zqkdService.articleShare(articleId)
        if (articleShare.error_code == "0") {
           log("【文章分享-$articleId】成功")
        } else {
           log("【文章分享-$articleId】失败：${articleShare.message}")
        }
    }

    /**
     * 阅读文章
     */
    private suspend fun readArticle(articleId: String, cateId: String, signature: String) {
        catchExp {
           log("【阅读文章-$articleId】开始 cateId = $cateId signature = $signature")
            zqkdService.articleContent(articleId, signature)
            val newArticleDetailResp = zqkdService.getNewArticleDetail(articleId, cateId, "0")
            val newArticleDetail = newArticleDetailResp.body()
            if (newArticleDetailResp.isSuccessful && newArticleDetail != null)
                if (newArticleDetail.error_code == "0") {
                   log("【阅读文章-$articleId】等待分享和完成。。。")
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
                   log("【阅读文章-$articleId】失败：${newArticleDetail.message}")
                }
        }
        delay(2000)
    }

    private suspend fun readArticleComplete(articleId: String, readType: String = "0") {
        catchExp {
            val reward = zqkdService.articleComplete(articleId, readType)
            if (reward.error_code == "0") {
                val score = reward.items?.read_score ?: 0
                successReward += score
               log("【完成阅读/视频-$articleId】获得 $score 青豆")
            } else {
               log("【完成阅读/视频-$articleId】失败：${reward.message}")
            }
        }
    }

    override fun getInterceptors(): List<Interceptor> {
        return listOf(
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val urlHead = request.header("kdYouth")
                    if (!urlHead.isNullOrEmpty()) {
                        val newRequest = request.newBuilder()
                        val newUrlBuilder = request.url.newBuilder()
                            .host(urlHead)
                        newRequest
                            .header("Origin", "https://kd.youth.cn")
                            .header("Sec-Fetch-Dest", "empty")
                            .header("Sec-Fetch-Mode", "cors")
                            .header("Sec-Fetch-Site", "same-origin")
                            .header(
                                "User-Agent",
                                " Mozilla/5.0 (Linux; Android 11; MI 6 Build/RQ3A.210605.005; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/91.0.4472.101 Mobile Safari/537.36"
                            )
                            .header("X-Requested-With", "XMLHttpRequest")

                        if (request.method.equals("GET", ignoreCase = true)) {
                            val commonParam = getYouthParams()
                            commonParam.forEach { k, v ->
                                newUrlBuilder.addQueryParameter(k,v)
                            }
                        } else if (request.method.equals("POST", ignoreCase = true)) {
                            if (request.body is FormBody) {
                                val newBodyBuilder = FormBody.Builder()
                                val body = request.body as FormBody
                                for (i in 0 until body.size) {
                                    newBodyBuilder.addEncoded(body.name(i), body.value(i))
                                }

                                val commonParam = getYouthParams()
                                commonParam.forEach { (k, v) ->
                                    newBodyBuilder.addEncoded(k,v)
                                }
                                newRequest.post(newBodyBuilder.build())
                            }
                        }
                        newRequest.url(newUrlBuilder.build())
                        val refer = request.header("Referer")
                        if (!refer.isNullOrEmpty()){
                            newRequest.header("Referer","${refer}?${getYouthParams().toUrlParam()}")
                        }

                        return chain.proceed(newRequest.build())
                    }

                    if (!request.url.host.contains("kandian.wkandian.com")) {
                        return chain.proceed(request)
                    }
                    val version = getVersion(request.url.encodedPathSegments[0])
                    val method = request.method
                    val extParam: TreeMap<String, String> = getExtraParams(version)
                    val response = if ("GET".equals(method, true)) {
                        for (name in request.url.queryParameterNames) {
                            extParam[name] = request.url.queryParameter(name) ?: ""
                        }
                        sign(extParam, version)
                        val encrypt = encryptParam(extParam)

                        val newRequest = request.newBuilder().header("device-platform", "android")
                            .url("${request.url.scheme}://${request.url.host}${request.url.encodedPath}?$PARAM_KEY=$encrypt")
                            .build()

                        chain.proceed(newRequest)
                    } else if ("POST".equals(method, true)) {
                        val body = request.body
                        if (body is FormBody) {
                            for (i in 0 until body.size) {
                                extParam[body.name(i)] = body.value(i)
                            }
                            sign(extParam, version)
                            val encrypt = encryptParam(extParam)

                            val newRequest = request.newBuilder()
                                .header("device-platform", "android")
                                .post(FormBody.Builder().add(PARAM_KEY, encrypt).build())
                                .build()
                            chain.proceed(newRequest)
                        } else {
                            chain.proceed(request)
                        }
                    } else {
                        chain.proceed(request)
                    }

                    if (!response.isSuccessful) {
                        return response
                    }

                    val responseBody = response.body ?: return response
                    val bodyString = responseBody.string()
//                    L.d("解密前字符串：$bodyString")
                    val decryptResp = try {
                        val resp = decryptResp(bodyString)
//                        L.d("解密后字符串：$resp")
                        resp
                    } catch (e: Exception) {
                        bodyString
                    }

                    return response.newBuilder()
                        .body(ResponseBody.create(responseBody.contentType(), decryptResp))
                        .build()
                }
            },
//            LoggerInterceptor()
        )
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

        for (i in 1..3){
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


    private suspend fun searchTaskList(){
        val body = youthService.sousuoNewInit()

        val searchBody = youthService.sousuoNewInit()

        if (searchBody.code == 0){
            val setting1 = youthService.setting(mUid, "pols104", "c8414341569dc5ad08294f8e11efbb48")

            if (setting1.finished_task < setting1.task_limit){

            }
        }

        println(body)
    }

    fun getYouthParams(): Map<String, String> {
        return mapOf(
            "access" to "WIFI",
            "app-version" to appVersion,
            "app_version" to appVersion,
            "channel" to "c4015",
            "cookie" to mZqkey,
            "cookie_id" to mZqkeyId,
            "device_brand" to "Xiaomi",
            "device_id" to "55860231",
            "device_model" to "MI 6",
            "device_platform" to "android",
            "device_type" to "android",
            "inner_version" to "202109141847",
            "mi" to "0",
            "os_api" to "30",
            "sm_device_id" to "2021100220074244c1349baf562f85dc18972d74a0696f01c23b885ac4e4df",
            "uid" to mUid,
            "zqkey" to mZqkey,
            "zqkey_id" to mZqkeyId
        )
    }

    fun getExtraParams(i: Int): TreeMap<String, String> {
        val channel = "c4015"
        val deviceId = "03c47748b842844b"
        val treeMap: TreeMap<String, String> = TreeMap<String, String>()
        treeMap["uid"] = mUid
        treeMap["language"] = "zh-CN"
        treeMap["os_version"] = "RQ3A.210605.005+release-keys"
        treeMap["os_api"] = "30"
        treeMap["device_brand"] = "Xiaomi"
        treeMap["device_model"] = "MI+6"
        treeMap["device_id"] = "55860231"
        treeMap["oaid"] = ""
        treeMap["s_im"] = ""
        treeMap["s_ad"] = "dCWwRj3eGxCw%3DLgpInxq08YdQNVIdvyDyigtEzPtXy0BO"
        treeMap["dpi"] = "480"
        treeMap["app_name"] = "zqkd_app"
        treeMap["app_version"] = appVersion
        treeMap["version_code"] = "64"
        treeMap["jssdk_version"] = ""
        treeMap["inner_version"] = "202109141847"
        treeMap["rom_version"] = "RQ3A.210605.005+release-keys"
        treeMap["channel"] = channel
        treeMap["network_type"] = "WIFI"
        treeMap["carrier"] = ""
        treeMap["ab_version"] = ""
        treeMap["ab_feature"] = ""
        treeMap["ab_client"] = ""
        treeMap["resolution"] = "1080x1920"
        treeMap["sm_device_id"] = "2021100220074244c1349baf562f85dc18972d74a0696f01c23b885ac4e4df"
        treeMap["zqkey_id"] = mZqkeyId
        treeMap["zqkey"] = mZqkey
        treeMap["mi"] = "1"
        treeMap["mobile_type"] = "1"
        treeMap["net_type"] = "1"
        treeMap["device_platform"] = "android"
        treeMap["storage"] = "110.25"
        treeMap["memory"] = "5"
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
            treeMap["version_name"] = "中青看点"
            treeMap["ts"] = (System.currentTimeMillis() / 1000.toLong()).toString()
        }
        return treeMap
    }


    companion object {

        const val SIGN_KEY_1 = "jdvylqchJZrfw0o2DgAbsmCGUapF1YChc"
        const val SIGN_KEY_2 = "zWpfzystJLrfw7o3SgGlMmGGPupK2YLhB"
        const val PARAM_KEY = "zqkd_param"

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

        fun getSecurityParamsByDES(str: String?): String {
            str ?: return ""
            val randomCode = chars.random()
            return getDisturbString(encryptDES(getSingInfo(randomCode), str), randomCode)
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

        fun decryptReq(str: String): String {
            val newStr = URLDecoder.decode(str, "UTF-8")
            val randomChar = newStr[0]
            val i = (randomChar.toInt() % '\n'.toInt()) % 3
            return decryptDES(getSingInfo(randomChar), newStr.substring(1, newStr.length - i))
        }

        fun decryptResp(str: String): String {
            val secretKeySpec =
                SecretKeySpec(parseSignature(appSignBase64Decode, 'a').substring(0, 16).toByteArray(), "AES")
            val instance = Cipher.getInstance("AES/ECB/PKCS5Padding")
            instance.init(2, secretKeySpec)
            return String(instance.doFinal(Base64.decode(str, 1)))
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


        private const val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        fun getDisturbString(str: String, c2: Char): String {
            val i: Int = (c2.toInt() % '\n'.toInt()) % 3
            val str2 = StringBuilder(c2.toString() + str)
            for (i2 in 0 until i) {
                str2.append(chars.random())
            }
            return str2.toString()
        }

        private const val appSign = """MIIDYTCCAkmgAwIBAgIESf2CUTANBgkqhkiG9w0BAQsFADBhMQswCQYDVQQGEwI4NjEQMA4GA1UE
    CBMHYmVpamluZzEQMA4GA1UEBxMHYmVpamluZzEOMAwGA1UEChMFeW91dGgxDjAMBgNVBAsTBXlv
    dXRoMQ4wDAYDVQQDEwV5b3V0aDAeFw0xNzAyMTcwOTE5MDNaFw00MjAyMTEwOTE5MDNaMGExCzAJ
    BgNVBAYTAjg2MRAwDgYDVQQIEwdiZWlqaW5nMRAwDgYDVQQHEwdiZWlqaW5nMQ4wDAYDVQQKEwV5
    b3V0aDEOMAwGA1UECxMFeW91dGgxDjAMBgNVBAMTBXlvdXRoMIIBIjANBgkqhkiG9w0BAQEFAAOC
    AQ8AMIIBCgKCAQEAguBri+pCOaaGyotA2l/SmHM32WEbOqSmUuPqIeQYGtV15QZqQhFmK2zL+FKn
    TuISZeq09hiHifg4CrpnygM8wJ19obbECvNBkc1qYTDoknWwhmqO6dtEC/aeTqGv3joR5IMG0aqy
    0Q9PE5fxDQCaNtN5BZgJf+gGUWuoQ8zRoKpjXhnKKFtsPhmbUWIldZtVjM9dqXeDVXxnk52qyeWG
    dbof/0ZWfbtIj8npUsbHn3nLx91Fc73/ah533C2iFUhGQrw2iEGISZ0eIZmeGYQnfKizfH1QhVsQ
    UCnA9tojzeowc6Cyrd06HPjSZFH8RLv4dBj0YlmC2wFUySXrwkcloQIDAQABoyEwHzAdBgNVHQ4E
    FgQU0vgBrrV+ZxAibmW+d7oWkAA6sDwwDQYJKoZIhvcNAQELBQADggEBAGzh4NPUl7XmPxAia3/E
    Vwp8TSfdZYwKFVmOSvQljTU2tZUoPFXEq7Inn0HyFawWEUl1jcWM5KibQrG0U01kH9qWlpPYnIr8
    e9b8YBVJi5q64MOiQOsH1xLJVBukmoBG/plheIvw7qYr6ofYd3LUi35bZ89fdaHqndxCrAViVvu5
    yA0rXSTc3p14QbcN1kb81uyZ2BgL91NAzMJRi71ChcIusl9dwMMScnBXNUbE5rnci4kc5kRmUAer
    ohqDzweI6hxUSty4FjLDptAsVKM9PHKn1h40IxtkRdWA+T515lbxapdtQhuCw1Glvn7CjxEuRAI+
    zzFZAeJDlknCoNskw1g="""

        val appSignBase64Decode = Base64.decode(appSign, 0)

        const val jwtKey =
            """AAAAB3NzaC1yc2EAAAADAQABAAABAQC1WAth281wjZj5XhGU9Iza5EXzOy5U/AKgGxF14svnCEWrTH6i3lZd+lMTFLvTakGI5l1RJmutFRku6CvDVCEc7dJURVWsrgQTFNBuu0t5WOkoUY0zNa05pejDmBC4w4MscH2OexCrKfHNEYi/FpjBJv1bwjU0luxt/cvsjBjlthgY47I4KNy+T953CpBiYQmkSJZUBzsN2Zz+jEA+CvLEK9BPHBlKcz0GupalgnHHSnS/JoUz8+RTjZr1O2sjSyrcg0LL+vWeCnJN07Uv4jJaTDqc6Ig1Mw+TJrrsARxoA+Frc66Qo7GFxACimuJ1LeCc9iFlMzZNZly3JxYAR019"""


    }
}