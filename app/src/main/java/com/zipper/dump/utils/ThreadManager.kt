package com.zipper.dump.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

object ThreadManager {

    /**
     * UI 线程handler
     */
    private val mMainHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * 子线程Handler
     */
    private val mSubHandler: Handler

    /**
     * 子线程
     */
    private val mSubThread: HandlerThread = HandlerThread("Sub-Thread")

    init {
        mSubThread.start()
        mSubHandler = Handler(mSubThread.looper)
    }

    fun runOnMain(task: Runnable) {
        mMainHandler.post(task)
    }

    fun runOnMain(task : () -> Unit ) {
        mMainHandler.post(task)
    }

    fun runOnSub(task: Runnable) {
        mSubHandler.post(task)
    }

    fun runOnSub(task: Runnable, delayMillis: Long) {
        mSubHandler.postDelayed(task, delayMillis)
    }

    fun removeOnMainCallback(task: Runnable) {
        mMainHandler.removeCallbacks(task)
    }

    fun removeOnSubCallback(task: Runnable) {
        mSubHandler.removeCallbacks(task)
    }

}