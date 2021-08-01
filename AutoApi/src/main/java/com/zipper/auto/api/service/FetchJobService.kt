package com.zipper.auto.api.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.zipper.auto.api.api.JJSApi
import com.zipper.auto.api.bean.ViewPoint
import com.zipper.auto.api.store.JJSDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FetchJobService: JobService() {

    val io = CoroutineScope(Dispatchers.IO)

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("FetchJobService", "onStartJob param = $params")
        io.launch{
            val dataList = JJSApi().index()
            val data: Array<ViewPoint> = dataList.toTypedArray()
            JJSDatabase.openDatabase(applicationContext).getJJSDao().insert(data)
            jobFinished(params,true)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("FetchJobService", "onStopJob param = $params")
        io.cancel()
        return false
    }
}