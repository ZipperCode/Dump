package com.zipper.dump.service

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.zipper.dump.R
import com.zipper.dump.activity.SplashActivity
import java.lang.ref.WeakReference


class GuardService : Service() {

    private var mServiceStatusText:String = SERVICE_STATUS_RUN_TEXT

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(mRefService == null){
            mRefService = WeakReference(this)
        }

        val status = intent?.getStringExtra(SERVICE_STATUS_KEY)

        status?.run {
            mServiceStatusText = this
        }
        if(Build.VERSION.SDK_INT >= 24){
            Log.d(TAG,"setForegroundService")
            setForegroundService()
        }

        return START_STICKY;
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG,"无障碍服务关闭，同时关闭前台服务")
        mRefService = null
        if(Build.VERSION.SDK_INT > 24){
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        super.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun setForegroundService() {
        //设定的通知渠道名称
        val channelName = "服务运行"
        //设置通知的重要程度
        val importance: Int = NotificationManager.IMPORTANCE_LOW
        //构建通知渠道
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
        channel.description = "我还活着"
        //在创建的通知渠道上发送通知
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_float) //设置通知图标
                .setContentTitle("Dump服务") //设置通知标题
                .setContentText(mServiceStatusText) //设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true) //设置处于运行状态
                .setContentIntent(PendingIntent.getActivity(this,0,
                    Intent(this,SplashActivity::class.java),0))
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build())
    }

    companion object{

        private val TAG: String = GuardService::class.java.simpleName
        const val CHANNEL_ID: String = "com.think.accessibility.service.GuardService"
        const val NOTIFICATION_ID: Int = 100

        var mRefService: WeakReference<GuardService>? = null

        const val SERVICE_STATUS_KEY = "GuardServiceStatus"
        const val SERVICE_STATUS_RUN_TEXT = "我还活着，跳跳跳。。。"
    }
}