package com.zipper.dump.fragment

import com.zipper.task.module.VariableHelper
import com.zipper.task.module.bean.VariableBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class VariableRepository {

    companion object {
        const val TAG = "VariableRepository"
    }

    val variableBeanFlow: Flow<List<VariableBean>> = flow {
        emit(VariableHelper.getVariableList())
    }.flowOn(Dispatchers.IO)

}