package com.zipper.dump.repo

import com.zipper.core.utils.SpUtil
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.SpHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/
class ServiceRepo {

    companion object{
        const val SERVICE_CTR_STATUS_KEY: String = "SERVICE_CTR_STATUS_KEY"
    }

    val serviceState: Flow<Boolean> = flow {
        emit(DumpService.mAccessibilityService != null)
    }

    private val _serviceCtrlState: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(SpUtil.instance(SpHelper.SP_NAME).get(SERVICE_CTR_STATUS_KEY, false))
    }

    val serviceCtrlState: StateFlow<Boolean> get() = _serviceCtrlState

    suspend fun refreshServiceState() = withContext(Dispatchers.IO){
        if (DumpService.mAccessibilityService == null && _serviceCtrlState.value){
            SpUtil.instance(SpHelper.SP_NAME).put(SERVICE_CTR_STATUS_KEY, false)
        }
        _serviceCtrlState.emit(SpUtil.instance(SpHelper.SP_NAME).get(SERVICE_CTR_STATUS_KEY, false))
    }

    suspend fun saveServiceCtrlState(value: Boolean) = withContext(Dispatchers.IO){
        SpUtil.instance(SpHelper.SP_NAME).put(SERVICE_CTR_STATUS_KEY, value)
        refreshServiceState()
    }
}