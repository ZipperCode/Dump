package com.zipper.api.module.bean

import androidx.annotation.IntDef
import com.zipper.api.module.util.CronExpression
import com.zipper.api.module.util.CronExpressionEx
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

data class ApiModuleInfo(
    val moduleKey: String,
    val moduleName: String,
    @ModuleStoreType
    var moduleStoreType: Int,
    val moduleFileName: String?,
    var modulePath: String,
    @ModuleType
    val moduleType: Int,
    val moduleImplClass: String,
    val moduleExecTime: String,
    var isBan: Boolean,
    var isRun: Boolean = false,
    var moduleVersion: Int = 100,
    var moduleMd5: String = "debug",
    val apiModuleTasks: List<ApiModuleTaskInfo>? = null
){
    /**
     * 当前执行时间
     */
    var currentExecTime: Long = 0L

    /**
     * 下一次执行时间
     */
    var nextExecTime: Long = 0L

    /**
     * 当前可执行的时间
     * @return  根据当前时间获取的可执行时间
     */
    fun parserCronTime(): Date{
        val cronExpressionEx = CronExpressionEx(moduleExecTime)
        return cronExpressionEx.getNextValidTimeAfter(Date())
    }

    fun getExecuteTimeFormat(): String{
        val date = parserCronTime()
        try{
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return sdf.format(date)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return "format error"
    }

    /**
     * 下一次执行时间
     * 相对于当前执行时间
     * @return  当前执行的下一次时间
     */
    fun getNextCronTime(): Date{
        val cronExpressionEx = CronExpressionEx(moduleExecTime)
        return cronExpressionEx.getNextValidTimeAfter(parserCronTime())
    }

    @IntDef(value = [ModuleType.TYPE_JAR, ModuleType.TYPE_AAR, ModuleType.TYPE_APK])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ModuleType{
        companion object{
            const val TYPE_JAR = 1
            const val TYPE_AAR = 2
            const val TYPE_APK = 3
        }
    }

    @IntDef(value = [ModuleStoreType.TYPE_ASSETS, ModuleStoreType.TYPE_LOCAL])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ModuleStoreType{
        companion object{
            const val TYPE_ASSETS = 1
            const val TYPE_LOCAL = 2
        }
    }
}
