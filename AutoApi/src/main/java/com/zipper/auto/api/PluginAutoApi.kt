package com.zipper.auto.api

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import com.zipper.auto.api.job.FetchWorker
import com.zipper.auto.api.store.JJSDatabase
import com.zipper.base.service.IPluginAutoApi
import com.zipper.core.BaseApp
import com.zipper.core.BasePlugin
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
class PluginAutoApi : BasePlugin(), IPluginAutoApi {

    companion object{

        const val TAG: String = "PluginAutoApi"

        lateinit var jjsDatabase: JJSDatabase

        @SuppressLint("BatteryLife")
        @RequiresApi(Build.VERSION_CODES.M)
        fun requestIgnoreBatteryOptimizations(){
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${app.packageName}")
                app.startActivity(intent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onApplicationCreate(baseApp: BaseApp) {
        super.onApplicationCreate(baseApp)
        jjsDatabase = JJSDatabase.openDatabase(baseApp)
    }

    override fun fetchData(context: Context?) {
        Log.d(TAG, "fetchData context = $context")
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(FetchWorker::class.java, 15L, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("FetchWorker")
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
            .build()
        context?.also { WorkManager.getInstance(context).enqueue(workRequest) }
    }
}