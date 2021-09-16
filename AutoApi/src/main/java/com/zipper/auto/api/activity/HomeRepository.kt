package com.zipper.auto.api.activity

import com.zipper.api.module.ApiModuleManager
import com.zipper.api.module.bean.ApiModuleInfo
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
        emit(ApiModuleManager.moduleInfoList)
    }

    fun addModule(apiModule: ApiModuleInfo){

    }

}