package com.zipper.dump.service

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.SpHelper
import kotlinx.coroutines.launch

class DumpService : AccessibilityService() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (AccessibilityHelper.mAccessibilityService == null) {
            AccessibilityHelper.mAccessibilityService = this
        }

        if (AccessibilityHelper.mDrawViewBound) {
            return
        }

        val pkName = event?.packageName?.toString() ?: ""
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            rootInActiveWindow?.run {
                // 过滤不需要处理的包
                if (AccessibilityHelper.pksContains(pkName)) {
                    dumpSplash(this, pkName)
                }
            }
        }
    }

    private fun dumpSplash(rootNodeInfo: AccessibilityNodeInfo, pks: String) {
        Log.d(TAG, "当前pks = $packageName 需要进行跳过处理")
        var clicked = false

        AccessibilityHelper.run {
            // 判断是否有自定义设定的viewId需要跳过
            val dumpViewIds = viewInfoListIds(pks)
            Log.d(TAG, "查找到的 dumpViewIds = $dumpViewIds")
            dumpViewIds.forEach {
                // 查找所有拥有当前id的view，处理跳过
                clicked = clicked or (findNodeById(rootNodeInfo, it)?.let { node ->
                    if (node.isClickable) {
                        click(node)
                    } else {
                        deepClick(node)
                    }
                } ?: false)
            }
        }

        if (!clicked) {
            val dumpName = SpHelper.loadString(pks)
            AccessibilityHelper.run {
                // 能查找到包含[跳过]文本的组件
                val dumpNode = findNodeByText(rootNodeInfo, if(TextUtils.isEmpty(dumpName)) "跳过" else dumpName)
                dumpNode?.let {
                    return@let if (it.isClickable) {
                        click(it)
                    } else {
                        deepClick(it)
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "中断")
        Toast.makeText(this, "无障碍服务中断", Toast.LENGTH_LONG).show();
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(this, "无障碍服务已打开", Toast.LENGTH_LONG).show();
        AccessibilityHelper.mAccessibilityService = this
        setForegroundService()
        App.mIoCoroutinesScope.launch {
            AccessibilityHelper.init(this@DumpService)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "无障碍服务关闭，请重新打开", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT > 24) {
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        return super.onUnbind(intent)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun setForegroundService() {
        //设定的通知渠道名称
        val channelName = "Dumping服务"
        //设置通知的重要程度
        val importance: Int = NotificationManager.IMPORTANCE_LOW
        //构建通知渠道
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
        channel.description = "Dumping服务进程"
        //在创建的通知渠道上发送通知
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_launch) //设置通知图标
            .setContentTitle("Dumping") //设置通知标题
            .setContentText("我还活着，跳跳跳。。。") //设置通知内容
            .setAutoCancel(true) //用户触摸时，自动关闭
            .setOngoing(true) //设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private val TAG = DumpService::class.java.simpleName
        const val CHANNEL_ID: String = "com.zipper.dump.service.DumpService"
        const val NOTIFICATION_ID: Int = 100
    }

}