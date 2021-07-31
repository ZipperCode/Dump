package com.zipper.dump.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.zipper.base.service.plugin.impl.AutoApiPao
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.activity.SplashActivity
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.core.utils.L
import com.zipper.core.utils.SpUtil
import com.zipper.dump.MainActivity
import com.zipper.dump.utils.SpHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DumpService : AccessibilityService() {

    private var lastCheckTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 当服务启动变量状态改变后，不处理任何无障碍。绘制页面时也不处理
        if (!serviceStatus or AccessibilityHelper.mDrawViewBound) {
            return
        }

        if(System.currentTimeMillis() - lastCheckTime > (5 * 60 * 1000)){
            lastCheckTime = System.currentTimeMillis()
            AutoApiPao.fetchData(this)
            L.d("FetchWorker 是否完成 ${WorkManager.getInstance(this).getWorkInfosForUniqueWork("FetchWorker")}")
            L.d("FetchWorker-Periodic 是否完成 ${WorkManager.getInstance(this).getWorkInfosForUniqueWork("FetchWorker-Periodic")}")
        }

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            windowContentChange(event)
        }
    }

    private fun windowContentChange(event: AccessibilityEvent?) {
        val pkName = event?.packageName?.toString() ?: ""
        rootInActiveWindow?.run {
            // 过滤不需要处理的包
            if (("com.miui.systemAdSolution" == pkName) or AccessibilityHelper.pksContains(pkName)) {
                dumpSplash(this, pkName)
            }else if("com.tencent.mm" == pkName){
//                if(AccessibilityHelper.mWxSettingValue){
//                    // 微信登陆自动
//                    val loginTitle = AccessibilityHelper.findNodeByText(rootInActiveWindow,"微信登陆确认")
//                    val loginCheck = AccessibilityHelper.findNodeByText(rootInActiveWindow,"同步最近消息")
//                    val loginButton = AccessibilityHelper.findNodeByText(rootInActiveWindow,"登陆")
//                    if((loginTitle != null) and (loginCheck != null) and (loginButton != null)){
//                        AccessibilityHelper.click(loginButton!!)
//                    }
//                }
            }
        }
    }

    private fun dumpSplash(rootNodeInfo: AccessibilityNodeInfo, pks: String) {
        L.d(TAG, "当前pks = $packageName 需要进行跳过处理")
        var clicked = false

        AccessibilityHelper.run {
            // 判断是否有自定义设定的viewId需要跳过
            val dumpViewIds = viewInfoListIds(pks)
            L.d(TAG, "查找到的 dumpViewIds = $dumpViewIds")
            dumpViewIds.forEach {
                // 查找所有拥有当前id的view，处理跳过
                clicked = clicked or (findNodeById(rootNodeInfo, it)?.let { node ->
                    click(node)
//                    if (node.isClickable) {
//                        click(node)
//                    } else {
//                        deepClick(node)
//                    }
                } ?: false)
            }
        }

        if (!clicked) {
            val dumpName = SpHelper.loadString(pks)
            AccessibilityHelper.run {
                // 能查找到包含[跳过]文本的组件
                val dumpNode = findNodeByText(
                    rootNodeInfo,
                    if (TextUtils.isEmpty(dumpName)) "跳过" else dumpName
                )
                dumpNode?.let {
                    click(it)
//                    return@let if (it.isClickable) {
//                        click(it)
//                    } else {
//                        deepClick(it)
//                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        L.d(TAG, "中断")
        Toast.makeText(this, "无障碍服务中断", Toast.LENGTH_LONG).show()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        mAccessibilityService = this
        serviceInfo = serviceInfo.apply {
            flags = flags or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }

        App.mIoCoroutinesScope.launch {
            AccessibilityHelper.init(this@DumpService)
            AccessibilityHelper.serviceRepo.refreshServiceState()
            AccessibilityHelper.serviceStatusFlow.collect {
                notifyServiceStatus(it)
            }
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "无障碍服务关闭，请重新打开", Toast.LENGTH_LONG).show()
        App.mMainCoroutinesScope.launch {
            AccessibilityHelper.closeServiceCtrl()
        }
        return super.onUnbind(intent)
    }

    fun notifyServiceStatus(status: Boolean) {
        mServiceStatus = status
        if (status) {
            setForegroundService()
        } else {
            if (Build.VERSION.SDK_INT > 24) {
                stopForeground(false)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NOTIFICATION_ID)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun setForegroundService() {
        //设定的通知渠道名称
        val channelName = "服务运行"
        //设置通知的重要程度
        val importance: Int = NotificationManager.IMPORTANCE_LOW
        //构建通知渠道
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
        channel.description = "我还活着"
        //在创建的通知渠道上发送通知
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
        builder.setSmallIcon(R.drawable.ic_float_24) //设置通知图标
            .setContentTitle("Dump服务") //设置通知标题
            .setContentText("我还活着，跳跳跳。。。") //设置通知内容
            .setAutoCancel(false) //用户触摸时，自动关闭
            .setOngoing(true) //设置处于运行状态
            .setContentIntent(
                PendingIntent.getActivity(
                    this, 0,
                    Intent(this, MainActivity::class.java), 0
                )
            )
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private val TAG = DumpService::class.java.simpleName
        const val CHANNEL_ID: String = "com.think.accessibility.service.DumpService"
        const val NOTIFICATION_ID: Int = 100
        var mAccessibilityService: DumpService? = null

        // 本地保存的服务是否开启变量
        var mServiceStatus: Boolean = false

        val serviceStatus: Boolean get() = ((mAccessibilityService != null) and mServiceStatus)
    }

}