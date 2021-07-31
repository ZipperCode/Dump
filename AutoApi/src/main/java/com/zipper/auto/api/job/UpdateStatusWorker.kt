package com.zipper.auto.api.job

import android.content.Context
import android.util.Log
import androidx.work.*
import com.zipper.auto.api.JJSApi
import com.zipper.auto.api.bean.ViewPoint
import com.zipper.auto.api.store.JJSDatabase
import com.zipper.core.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class UpdateStatusWorker(context: Context, param: WorkerParameters): CoroutineWorker(context, param) {

    private val mutex = Mutex()

    override suspend fun doWork(): Result {
        Log.d(TAG,"doWork 正在执行任务, 当前时间为： ${(System.currentTimeMillis() / 1000).format()}")
        val result =  withContext(Dispatchers.IO){
            return@withContext try {
                mutex.withLock{
                    JJSDatabase.openDatabase(applicationContext)
                        .getBaseJJSDao().updateOverDue(System.currentTimeMillis())
                }
                Result.success()
            }catch (e: Exception){
                e.printStackTrace()
                Result.retry()
            }
        }
        Log.d(TAG,"任务执行结果 ${result}, 当前时间为： ${(System.currentTimeMillis() / 1000).format()}")
        return result
    }

    companion object{

        const val TAG: String = "UpdateStatusWorker"

        fun periodicWork(): PeriodicWorkRequest{
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            return PeriodicWorkRequest.Builder(UpdateStatusWorker::class.java, 1L, TimeUnit.DAYS)
                .setConstraints(constraints)
                .addTag("UpdateStatusWorker-Periodic")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
                .build()
        }

        fun uniquePeriodic(context: Context){
            // 如果存在作业，则本次作业不执行
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "UpdateStatusWorker-Periodic",ExistingPeriodicWorkPolicy.KEEP, periodicWork())
        }
    }

}