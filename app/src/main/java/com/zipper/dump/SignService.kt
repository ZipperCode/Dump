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
import com.zipper.sign.gdbh.GdbhApi
import com.zipper.sign.jckd.JckdApi
import com.zipper.sign.jckd.JckdApiParam
import com.zipper.sign.kugou.ConfigBean
import com.zipper.sign.kugou.KgTaskApi
import com.zipper.sign.zqkd.ZqkdApi
import com.zipper.sign.zqkd.ZqkdApiParam
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

        val kgState: MutableLiveData<Boolean> = MutableLiveData(false)
        val zqState: MutableLiveData<Boolean> = MutableLiveData(false)
        val jcState: MutableLiveData<Boolean> = MutableLiveData(false)
        val gdState: MutableLiveData<Boolean> = MutableLiveData(false)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        when(intent?.action ?: "START"){
            START -> notifyServiceStatus(true)
            STOP -> notifyServiceStatus(false)
            ZQKD -> doZQ()
            KUGOU -> doKG()
            JCKD -> doJC()
            GUBH -> doGD()
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

    fun doKG(){
        if (kgState.value == true){
            return
        }
        kgState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val json = SpUtil.instance(MainActivity.TASK_SP_NAME).get(Constant.SP_KEY_KG,"[]")
            val userList: List<ConfigBean> = Gson().fromJson(json, object : TypeToken<List<ConfigBean>>(){}.type)
            userList.map {
                async {
                    val task = KgTaskApi(it)
                    task.execute()
                    task
                }
            }.forEach {
                val res = it.await()
            }
            withContext(Dispatchers.Main){
                kgState.value = false
            }
        }
    }

    fun doZQ(){
        if (zqState.value == true){
            return
        }
        zqState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val json = SpUtil.instance(MainActivity.TASK_SP_NAME).get(Constant.SP_KEY_ZQ,"[]")
            val list: List<ZqkdApiParam> = Gson().fromJson(json, object : TypeToken<List<ZqkdApiParam>>() {}.type)
            list.map {
                async {
                    val task = ZqkdApi()
                    task.execute(it)
                    task
                }
            }.forEach {
                val res = it.await()
            }
            withContext(Dispatchers.Main){
                zqState.value = false
            }
        }

    }

    fun doJC(){
        if (jcState.value == true){
            return
        }
        jcState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val json = SpUtil.instance(MainActivity.TASK_SP_NAME).get(Constant.SP_KEY_JC,"[]")
            val list: List<JckdApiParam> = Gson().fromJson(json, object : TypeToken<List<JckdApiParam>>() {}.type)
            list.map {
                async {
                    val task = JckdApi()
                    task.execute(it)
                    task
                }
            }.forEach {
                val res = it.await()
            }
            withContext(Dispatchers.Main){
                jcState.value = false
            }
        }
    }

    fun doGD(){
        if (gdState.value == true){
            return
        }
        gdState.value = true

        CoroutineScope(Dispatchers.IO).launch {
            GdbhApi().testExecute()
            withContext(Dispatchers.Main){
                gdState.value = false
            }
        }
    }


}