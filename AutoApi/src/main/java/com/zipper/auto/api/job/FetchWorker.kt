package com.zipper.auto.api.job

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zipper.auto.api.JJSApi
import com.zipper.auto.api.ViewPoint
import com.zipper.auto.api.store.JJSDatabase
import com.zipper.core.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class FetchWorker(context: Context, param: WorkerParameters): CoroutineWorker(context, param) {

    val api = JJSApi()

    override suspend fun doWork(): Result {
        Log.d("FetchWorker","doWork 正在执行任务, 当前时间为： ${System.currentTimeMillis().format()}")
        val result =  withContext(Dispatchers.IO){
            return@withContext try {
                val dataList = api.index()
                val data: Array<ViewPoint> = dataList.toTypedArray()
                JJSDatabase.openDatabase(applicationContext).getJJSDao().insert(*data)
                Result.success()
            }catch (e: Exception){
                Result.retry()
            }
        }
        Log.d("FetchWorker","任务执行结果 ${result}, 当前时间为： ${System.currentTimeMillis().format()}")
        return result
    }

}