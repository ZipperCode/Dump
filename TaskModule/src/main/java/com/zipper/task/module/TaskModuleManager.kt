package com.zipper.task.module

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.utils.SpUtil
import com.zipper.sign.core.BaseTask
import com.zipper.sign.jckd.JckdTaskImpl
import com.zipper.sign.zqkd.ZqkdTaskImpl
import kotlinx.coroutines.Deferred

object TaskModuleManager {

    private const val SP_NAME = "task_info"
    private const val SP_KEY_TASK_INFO = "task_info_json"
    private const val SP_TASK_CONFIG_NAME = "task_config"

    private lateinit var appContext: Context

    val taskInfoMap: MutableMap<String, TaskModuleInfo> = mutableMapOf()

    private val runTaskInfo: MutableMap<String, Deferred<BaseTask>> = mutableMapOf()

    fun initModule(context: Context){
        appContext = context.applicationContext
        initTask()
    }

    private fun initTask(){
        taskInfoMap["zqkd"] = TaskModuleInfo("zqkd","中青看点",ZqkdTaskImpl::class.java.name)
        taskInfoMap["jckd"] = TaskModuleInfo("jckd","晶彩看点",ZqkdTaskImpl::class.java.name)
    }

    fun getTaskMain(moduleKey: String): String = taskInfoMap[moduleKey]?.moduleImpl ?: ""

    fun taskIsRun(moduleKey: String) = runTaskInfo.containsKey(moduleKey)

    /**
     * 执行任务
     */
    suspend fun runTask(moduleKey: String): Result<Boolean>{
        val moduleImpl = getTaskMain(moduleKey)

        if (TextUtils.isEmpty(moduleImpl)){
            return Result.failure(Throwable("未找到${moduleKey}的实现类: $moduleImpl"))
        }
        return try{
            val baseTask: BaseTask = Class.forName(moduleImpl).newInstance() as BaseTask
            val configJson = SpUtil.instance(SP_TASK_CONFIG_NAME).get(moduleKey, "[]")
            runTaskInfo[moduleKey] = baseTask.execute(configJson).apply {
                invokeOnCompletion {
                    removeRunTask(moduleKey)
                    baseTask.destroy()
                }
            }
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    /**
     * 停止任务
     */
    fun stopTask(moduleKey: String): Result<Boolean>{
        return try {
            runTaskInfo.remove(moduleKey)?.run {
                cancel()
            }
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    /**
     * 禁用模块
     */
    fun banOrResumeTask(moduleKey: String, isBan: Boolean = true): Result<Boolean>{
        return try {
            val moduleInfo = taskInfoMap[moduleKey] ?: return Result.failure(Exception("module not found"))
            val newModuleInfo = moduleInfo.copy(isBan = isBan)
            taskInfoMap[moduleKey] = newModuleInfo
            val taskInfoJson = Gson().toJson(taskInfoMap)
            SpUtil.instance(SP_NAME).putAsync(SP_KEY_TASK_INFO, taskInfoJson)
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    /**
     * 删除模块
     */
    fun delTask(moduleKey: String): Result<Boolean>{
        return try {
            taskInfoMap.remove(moduleKey)
            val taskInfoJson = Gson().toJson(taskInfoMap)
            SpUtil.instance(SP_NAME).putAsync(SP_KEY_TASK_INFO, taskInfoJson)
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    private fun removeRunTask(moduleKey: String){
        synchronized(runTaskInfo){
            runTaskInfo.remove(moduleKey)
        }
    }


}