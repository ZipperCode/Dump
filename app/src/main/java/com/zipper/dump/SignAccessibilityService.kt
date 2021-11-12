package com.zipper.dump

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zipper.core.utils.L
import java.lang.Exception

class SignAccessibilityService : AccessibilityService() {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
        L.d(TAG, "中断")
        Toast.makeText(this, "无障碍服务中断", Toast.LENGTH_LONG).show()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        mAccessibilityAccessibilityService = this
        serviceInfo = serviceInfo.apply {
            flags = flags or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        try {
            val intent = Intent(this, SignService::class.java)
            intent.action = "START"
            startService(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "无障碍服务关闭，请重新打开", Toast.LENGTH_LONG).show()
        try {
            val intentService = Intent(this, SignService::class.java)
            stopService(intentService)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return super.onUnbind(intent)
    }

    companion object {
        private val TAG = SignAccessibilityService::class.java.simpleName
        const val CHANNEL_ID: String = "com.zipper.sign.service.SignService"
        const val NOTIFICATION_ID: Int = 100
        var mAccessibilityAccessibilityService: SignAccessibilityService? = null
        // 本地保存的服务是否开启变量
        var mServiceStatus: Boolean = false
    }

}