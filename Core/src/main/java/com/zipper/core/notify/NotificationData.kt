package com.zipper.core.notify

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

open class NotificationData(
    val title: String,
    val content: String,
    val channelId: String,
    val channelName: String
){
    open var priority: Int = NotificationCompat.PRIORITY_DEFAULT
    open var channelDescription: String = ""
    open var channelImportance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
    open var channelEnableVibrate: Boolean = false
    open var channelLockScreenVisibility: Int = NotificationCompat.VISIBILITY_PUBLIC
}


