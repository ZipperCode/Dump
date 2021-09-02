package com.zipper.auto.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.work.*
import com.zipper.auto.api.activity.ApiActivity
import com.zipper.auto.api.job.FetchWorker
import com.zipper.auto.api.job.UpdateStatusWorker
import com.zipper.auto.api.store.JJSDatabase
import com.zipper.base.service.IPluginAutoApi
import com.zipper.core.BaseApp
import com.zipper.core.plugin.BasePlugin
import com.zipper.core.utils.LaunchUtil

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
class PluginAutoApi : BasePlugin(), IPluginAutoApi {

    companion object{

        const val TAG: String = "PluginAutoApi"

        lateinit var jjsDatabase: JJSDatabase
    }

    override fun onApplicationCreate(baseApp: BaseApp) {
        super.onApplicationCreate(baseApp)
        jjsDatabase = JJSDatabase.openDatabase(baseApp)
    }

    override fun fetchData(context: Context?) {
        Log.d(TAG, "fetchData context = $context")
        val ctx = context ?: app
        WorkManager.getInstance(ctx).enqueueUniqueWork(
            "FetchWorker",
            ExistingWorkPolicy.REPLACE,
            FetchWorker.onceWork()
        )
        UpdateStatusWorker.uniquePeriodic(ctx)
    }

    override fun onMainActivityCreate(activity: Activity) {
        super.onMainActivityCreate(activity)
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val powerManager = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
            if(!powerManager.isIgnoringBatteryOptimizations(activity.packageName)){
                LaunchUtil.toIgnoreBatteryOptimization(activity)
            }
        }
    }

    override fun startListActivity() {
        val intent = Intent(app,ViewPointActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }


    override fun startJdActivity() {
        val intent = Intent(app,ApiActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }
}
