package com.example.dumping.service

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.dumping.R
import com.example.dumping.utils.AccessibilityUtil
import com.example.dumping.utils.ThreadManager

class DumpAccessibilityService : AccessibilityService() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (AccessibilityUtil.mAccessibilityService == null) {
            AccessibilityUtil.mAccessibilityService = this
        }
        val pkName = event?.packageName?.toString() ?: ""
//        val className = event?.className?.toString() ?: ""
        when (event?.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                rootInActiveWindow?.run {
                    // 过滤不需要处理的包
                    if (AccessibilityUtil.pksContains(pkName) and !AccessibilityUtil.mDrawViewBound) {
                        ThreadManager.runOnSub{
                            dumpSplash(this,pkName)
                        }
                    }
                }
            }
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                Log.d(TAG, "窗口内容改变")
//            }
        }
    }

    private fun dumpSplash(rootNodeInfo: AccessibilityNodeInfo, pks: String) {
        // 能查找到[跳过]的组件
        var clicked = AccessibilityUtil
            .findNodeByText(rootNodeInfo, "跳过")
            ?.let {
                return@let if (it.isClickable) {
                    AccessibilityUtil.click(it)
                } else {
                    AccessibilityUtil.deepClick(it)
                }
            } ?: false

        if(!clicked){
            Log.d(TAG,"无法跳过包含跳过的组件，使用id进行查找")
            // 判断是否有自定义设定的viewId需要跳过
            val dumpViewIds = AccessibilityUtil.viewInfoListIds(pks)
            dumpViewIds.forEach {
                // 查找所有拥有当前id的view，处理跳过
                AccessibilityUtil.findNodeById(rootNodeInfo,it)?.run {
                    Log.d(TAG,"处理id= $it 的跳过")
                    if (isClickable) {
                        AccessibilityUtil.click(this)
                    } else {
                        AccessibilityUtil.deepClick(this)
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
        AccessibilityUtil.mAccessibilityService = this
        setForegroundService()
        ThreadManager.runOnSub {AccessibilityUtil.init(this)}
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
        builder.setSmallIcon(R.drawable.ic_float) //设置通知图标
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
        private val TAG = DumpAccessibilityService::class.java.simpleName
        const val CHANNEL_ID: String = "com.example.dumping.service.DumpAccessibilityService"
        const val NOTIFICATION_ID: Int = 100
    }

}