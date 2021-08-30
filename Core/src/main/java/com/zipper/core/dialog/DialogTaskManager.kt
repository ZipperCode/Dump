package com.zipper.core.dialog

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.zipper.core.utils.L
import kotlinx.coroutines.*
import java.lang.Runnable
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

object DialogTaskManager {

    const val TAG: String = "DialogTaskManager"

    fun runTaskWithLoading(activity: FragmentActivity, task: Runnable, timeout: Long = 10000L, canCancel: Boolean = true): Job?{
        val tag = TaskLoadingDialog::javaClass.name
        val preLoadingDialog = activity.supportFragmentManager.findFragmentByTag(tag)
        L.d(TAG, "preLoadingDialog = $preLoadingDialog")
        if(preLoadingDialog != null){
            L.d(TAG, "请稍后再试")
            return null
        }

        val loadingDialog = TaskLoadingDialog()

        val job =  CoroutineScope(Dispatchers.Default).launch {
            try{
                withTimeout(timeout){
                    withContext(Dispatchers.Main){
                        loadingDialog.show(activity)
                    }
                    L.d(TAG, "开始执行任务")
                    task.run()
                    L.d(TAG, "执行任务完成")
                }
            }finally {
                L.d(TAG, "任务超时或者已经完成，释放LoadingDialog")
                withContext(Dispatchers.Main){
                    loadingDialog.release()
                }
            }
        }

        loadingDialog.withTaskJob(job).canCancel(canCancel)

        return job
    }


    fun runTaskWithLoading(activity: FragmentActivity, block: suspend CoroutineScope.() -> Unit,
                           timeout: Long = 10000L, canCancel: Boolean = true): Job?{
        val tag = TaskLoadingDialog::javaClass.name
        val preLoadingDialog = activity.supportFragmentManager.findFragmentByTag(tag)
        L.d(TAG, "preLoadingDialog = $preLoadingDialog")
        if(preLoadingDialog != null){
            L.d(TAG, "请稍后再试")
            return null
        }

        val loadingDialog = TaskLoadingDialog()

        val job =  CoroutineScope(Dispatchers.Default).launch {
            try{
                withTimeout(timeout){
                    withContext(Dispatchers.Main){
                        loadingDialog.show(activity)
                    }
                    L.d(TAG, "开始执行任务")
                    block.invoke(this)
                    L.d(TAG, "执行任务完成")
                }
            }finally {
                L.d(TAG, "任务超时或者已经完成，释放LoadingDialog")
                withContext(Dispatchers.Main){
                    loadingDialog.release()
                }
            }
        }

        loadingDialog.withTaskJob(job).canCancel(canCancel)

        return job
    }
}