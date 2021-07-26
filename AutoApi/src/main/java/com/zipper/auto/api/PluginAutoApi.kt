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
import com.zipper.core.LaunchUtil
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
        val ctx = context ?: app
        WorkManager.getInstance(ctx).cancelAllWork()
        WorkManager.getInstance(ctx).enqueueUniqueWork(
            "FetchWorker-1",
            ExistingWorkPolicy.REPLACE,
            FetchWorker.onceWork()
        )
    }

    override fun startListActivity() {
        val intent = Intent(app,ViewPointActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }
}
