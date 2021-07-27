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

class FetchWorker(context: Context, param: WorkerParameters): CoroutineWorker(context, param) {

    val api = JJSApi()

    private val mutex = Mutex()

    override suspend fun doWork(): Result {
        Log.d(TAG,"doWork 正在执行任务, 当前时间为： ${(System.currentTimeMillis() / 1000).format()}")
        val result =  withContext(Dispatchers.IO){
            return@withContext try {
                val dataList = api.index()
                Log.d(TAG,"dataList len = ${dataList.size}")
                mutex.withLock{
                    JJSDatabase.openDatabase(applicationContext).getBaseJJSDao().insertAll(dataList)
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

    fun next(){
        WorkManager.getInstance(applicationContext).enqueue(nextWork())
    }

    companion object{

        const val TAG: String = "FetchWorker"

        fun onceWork(): OneTimeWorkRequest{
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()
            return OneTimeWorkRequest.Builder(FetchWorker::class.java)
                .setConstraints(constraints)
                .addTag("FetchWorker")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
                .build()
        }

        fun nextWork(): OneTimeWorkRequest{
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            return OneTimeWorkRequest.Builder(FetchWorker::class.java)
                .setConstraints(constraints)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("FetchWorker")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
                .build()
        }

        fun periodicWork(): PeriodicWorkRequest{
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            return PeriodicWorkRequest.Builder(FetchWorker::class.java, 15L, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag("FetchWorker-Periodic")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
                .build()
        }

        fun uniquePeriodic(context: Context, workRequest: PeriodicWorkRequest){
            // 如果存在作业，则本次作业不执行
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "FetchWorker",ExistingPeriodicWorkPolicy.KEEP, workRequest)
        }
    }

}