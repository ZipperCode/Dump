package com.zipper.core.notify

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zipper.core.ext.notificationManager

/**
 *  @author zipper
 *  @date 2021-08-13
 *  @description
 **/
object NotificationUtil {

    @JvmStatic
    @SuppressLint("WrongConstant")
    fun createNotificationChannel(context: Context, notificationData: NotificationData): String{
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val channelId = notificationData.channelId
            val channelName = notificationData.channelName
            val channelDescription = notificationData.channelDescription
            val channelImportance = notificationData.channelImportance
            val channelEnableVibrate = notificationData.channelEnableVibrate
            val channelLockScreenVisibility = notificationData.channelLockScreenVisibility

            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
                enableVibration(channelEnableVibrate)
                lockscreenVisibility = channelLockScreenVisibility
            }
            context.notificationManager().createNotificationChannel(notificationChannel)
            channelId
        }else ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun openChannelSetting(context: Context, channelId: String){
        val intent = with(Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        }
        context.startActivity(intent)
    }
}