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
        // 服务未启动，无法进行处理
        if(!App.serviceStatus){
            AccessibilityHelper.mAccessibilityService = null
            return
        }

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
        AccessibilityHelper.mAccessibilityService = this
        App.mIoCoroutinesScope.launch {
            AccessibilityHelper.init(this@DumpService)
            if(SpHelper.loadBoolean(SpHelper.SP_SERVICE_STATUS_KEY)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this@DumpService,GuardService::class.java))
                }else{
                    startService(Intent(this@DumpService,GuardService::class.java))
                }
            }
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "无障碍服务关闭，请重新打开", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT > 24) {
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        return super.onUnbind(intent)
    }


    companion object {
        private val TAG = DumpService::class.java.simpleName
        const val CHANNEL_ID: String = "com.zipper.dump.service.DumpService"
        const val NOTIFICATION_ID: Int = 100
    }

}