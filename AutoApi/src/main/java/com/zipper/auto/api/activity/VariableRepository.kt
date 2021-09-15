package com.zipper.auto.api.activity

import com.zipper.api.module.bean.ApiVariableBean
import com.zipper.auto.api.activity.bean.VariableItemBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VariableRepository {

    val apiModuleListFlow: Flow<List<ApiVariableBean>> = flow {
        emit(listOf(
            ApiVariableBean(
                "变量1",
                "变量1",
                "变量1",
                false,
                false,
                listOf()
            )
        ))
    }
}