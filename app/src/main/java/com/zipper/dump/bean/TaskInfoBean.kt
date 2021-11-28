package com.zipper.dump.bean


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

data class TaskInfoBean(
    val id: Int,
    val title: String,
    val info: ObservableField<String> = ObservableField(""),
    val deepLink: String = "",
    val task: BusinessTaskInfo? = null,
    val isRunning: ObservableBoolean = ObservableBoolean(false)
){

    suspend fun execute() = withContext(Dispatchers.Main){
        isRunning.set(true)
        try {
            if(task == null) return@withContext
            withContext(Dispatchers.IO){
                val clazz = Class.forName(task.className)
                val method = clazz.getDeclaredMethod(task.method)
                val targetObj = clazz.newInstance()
                method.invoke(targetObj)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            isRunning.set(false)
        }

    }

    fun dump(){

    }
}
