package com.zipper.auto.api.activity

import com.zipper.api.module.ApiModuleInfo
import com.zipper.api.module.ApiModuleTaskInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/14
 **/
class HomeRepository {

    val apiModuleListFlow: Flow<List<ApiModuleInfo>> = flow {
        emit(mutableListOf(
            ApiModuleInfo(
                "moduleKey",
                "测试模块",
                "路径",
                ApiModuleInfo.ModuleType.TYPE_AAR,
                "com....",
                100000L,
                mutableListOf(
                    ApiModuleTaskInfo(
                        "testModuleTask1",
                        "模块测试任务1",
                        "任务描述",
                        10000L
                    ),
                    ApiModuleTaskInfo(
                        "testModuleTask1",
                        "模块测试任务1",
                        "任务描述",
                        10000L
                    ),
                    ApiModuleTaskInfo(
                        "testModuleTask1",
                        "模块测试任务1",
                        "任务描述",
                        10000L
                    )
                )
            )
        ))
    }

    fun addModule(apiModule: ApiModuleInfo){

    }

}