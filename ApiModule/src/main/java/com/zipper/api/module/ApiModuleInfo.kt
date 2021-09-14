package com.zipper.api.module

import androidx.annotation.IntDef

data class ApiModuleInfo(
    val moduleKey: String,
    val moduleName: String,
    @ModuleStoreType
    val moduleStoreType: Int,
    val modulePath: String,
    @ModuleType
    val moduleType: Int,
    val moduleImplClass: String,
    val moduleExecTime: String,
    val apiModuleTasks: List<ApiModuleTaskInfo>? = null
){

    @IntDef(value = [ModuleType.TYPE_JAR, ModuleType.TYPE_AAR,ModuleType.TYPE_APK])
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
