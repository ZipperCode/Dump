package com.zipper.dump.activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.zipper.core.ext.statusBarAdaptive
import com.zipper.dump.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if(Build.VERSION.SDK_INT > 23){
            window.decorView.statusBarAdaptive(true)
        }

        setContentView(R.layout.activity_test)

        WindowCompat.setDecorFitsSystemWindows(window, true)


        val btnBigText: Button = findViewById(R.id.btn_big_text)

        btnBigText.setOnClickListener {
            val data = NotificationData(
                "我是title","我是content", NotificationCompat.PRIORITY_DEFAULT,
                "notification_channel_id" + Random(100).nextInt(), "ChannelName",
                "ChannelDescription", NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
            val channelId = createNotificationChannel(data)

            val bigTextStyle = NotificationCompat.BigTextStyle()
                .bigText("我是BigTextfadsoifhpsidfhpaosidfhpsoduifhpaosidhfpaoisdhfpos dfho uidhfasodufhasd ofihoweirpuweoiru")
                .setBigContentTitle("我是BigContentTitle")
                .setSummaryText("我是SummaryText")

            val notifyIntent = Intent(this, TestActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }

            val notifyPendingIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_weixin_24)
                .setContentTitle("我是title" + Random(100).nextInt())
                .setContentText("我是Text" + Random(100).nextInt())
                .setContentInfo("我是info" + Random(100).nextInt())
                .setPriority(data.channelImportance)
                .setStyle(bigTextStyle)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(notifyPendingIntent)
                .setVisibility(data.channelLockScreenVisibility)
                .setAutoCancel(true)

            lifecycleScope.launch {
                with(NotificationManagerCompat.from(applicationContext)) {
                    if(areNotificationsEnabled()){
                        notificationBuilder.setContentText("正在下载")
                        notificationBuilder.setProgress(100, 0, false)
                        notify(100, notificationBuilder.build())
                        var i = 0
                        val n = 100
                        while (i++ < n){
                            delay(100)
                            notificationBuilder.setProgress(100, i, false)
                            notify(100, notificationBuilder.build())
                        }
                        notificationBuilder.setContentText("下载完成")
                        notificationBuilder.setProgress(0, 0, false)
                        notify(100, notificationBuilder.build())
                    }
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun createNotificationChannel(notificationData: NotificationData): String{
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val channelId = notificationData.channelId
            val channelName = notificationData.channelName
            val channelDescription = notificationData.channelDescription
            val channelImportance = notificationData.channelImportance
            val channelEnableVibrate = notificationData.channelEnableVibrate
            val channelLockScreenVisibility = notificationData.channelLockScreenVisibility

//            val notificationChannel = NotificationChannelCompat
//                .Builder(channelId, channelImportance)
//                .setName(channelName)
//                .setDescription(channelDescription)
//                .setVibrationEnabled(channelEnableVibrate)
//                .build()
            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
                enableVibration(channelEnableVibrate)
                lockscreenVisibility = channelLockScreenVisibility
            }

            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(notificationChannel)

            channelId
        }else ""
    }
}

data class NotificationData(
    val title: String,
    val content: String,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    val channelId: String,
    val channelName: String,
    val channelDescription: String = "",

    val channelImportance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    val channelEnableVibrate: Boolean = false,
    val channelLockScreenVisibility: Int = NotificationCompat.VISIBILITY_PUBLIC
)