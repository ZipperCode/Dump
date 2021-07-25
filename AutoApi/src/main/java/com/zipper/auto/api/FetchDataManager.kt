package com.zipper.auto.api

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import com.zipper.auto.api.service.FetchJobService


object FetchDataManager {

    fun fetch(context: Context){
        var mJobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val builder = JobInfo.Builder(
            100,
            ComponentName(context.packageName, FetchJobService::class.java.getName())
        ).setBackoffCriteria(1000L, JobInfo.BACKOFF_POLICY_LINEAR )
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(60 * 1000)
            .build()
        val result = mJobScheduler.schedule(builder)
        Log.d("FetchDataManager", "result = $result")

    }


}