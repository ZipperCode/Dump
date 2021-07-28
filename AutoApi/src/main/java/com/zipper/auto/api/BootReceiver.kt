package com.zipper.auto.api

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zipper.core.utils.L

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED){
            L.d("应用自启广播")
//            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//                "FetchWorker-Periodic",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                FetchWorker.periodicWork()
//            )
        }
    }
}