package com.zipper.dump

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
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.utils.SpUtil
import com.zipper.task.module.TaskModuleManager
import kotlinx.coroutines.*

/**
 *
 * @author zhangzhipeng
 * @date   2021/11/12
 **/
class SignService: Service() {
    companion object{
        const val TAG = "SignService"
        const val START = "START"
        const val STOP = "STOP"
        const val ZQKD = "ZQKD"
        const val KUGOU = "KUGOU"
        const val JCKD = "JCKD"
        const val GUBH = "GUBH"

        /**
         * 开启服务
         */
        const val ACTION_START_SERVICE = "action.start_service"

        /**
         * 停止服务
         */
        const val ACTION_STOP_SERVICE = "action.stop_service"

        /**
         * 添加通知
         */
        const val ACTION_ADD_NOTIFY = "action.add_notify"

        /**
         * 更新通知
         */
        const val ACTION_UPDATE_NOTIFY = "action.update_notify"

        /**
         * 运行任务
         */
        const val ACTION_RUN_TASK = "action.run_task"

        /**
         * 停止任务
         */
        const val ACTION_STOP_TASK = "action.stop_task"

        /**
         * 服务行为
         */
        const val INTENT_PARAM_ACTION_KEY = "action"
        const val INTENT_PARAM_TASK_KEY = "intent.param.task_key"

        /**
         * 是否需要弹出服务开启
         */
        val alertSignServiceDialog: MutableLiveData<Boolean> = MutableLiveData(true)
    }

    private val ioCoroutine: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        alertSignServiceDialog.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        alertSignServiceDialog.value = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        when(intent?.action){
            ACTION_START_SERVICE -> notifyServiceStatus(true)
            ACTION_STOP_SERVICE -> notifyServiceStatus(false)
            ACTION_RUN_TASK -> runTask(intent.getStringExtra(INTENT_PARAM_TASK_KEY))
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun notifyServiceStatus(status: Boolean) {
        SignAccessibilityService.mServiceStatus = status
        if (status) {
            setForegroundService()
        } else {
            if (Build.VERSION.SDK_INT > 24) {
                stopForeground(false)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(SignAccessibilityService.NOTIFICATION_ID)
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
        val channel = NotificationChannel(SignAccessibilityService.CHANNEL_ID, channelName, importance)
        channel.description = "我还活着"
        //在创建的通知渠道上发送通知
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            SignAccessibilityService.CHANNEL_ID
        )
        builder.setSmallIcon(R.drawable.ic_launch) //设置通知图标
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
        startForeground(SignAccessibilityService.NOTIFICATION_ID, builder.build())
    }

    /**
     * 运行任务
     */
    private fun runTask(taskKey: String?){
        if (taskKey.isNullOrEmpty()){
            Log.d(TAG,"任务运行失败，原因：任务key为空")
            return
        }
        if (TaskModuleManager.taskIsRun(taskKey)){
            Log.d(TAG,"任务运行失败，原因：任务已经在运行了")
            return
        }
        ioCoroutine.launch {
            TaskModuleManager.runTask(taskKey)
        }
    }

    /**
     * 任务停止运行
     */
    private fun stopTask(taskKey: String?){
        if (taskKey.isNullOrEmpty()){
            Log.d(TAG,"任务停止运行失败，原因：任务key为空")
            return
        }
        if (!TaskModuleManager.taskIsRun(taskKey)){
            Log.d(TAG,"任务运行失败，原因：任务没有在运行")
            return
        }
        TaskModuleManager.stopTask(taskKey)
    }



}