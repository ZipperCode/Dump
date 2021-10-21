package com.zipper.plugin.dump.repo

import com.zipper.core.utils.SpUtil
import com.zipper.plugin.dump.service.DumpService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/
class ServiceRepo {

    companion object{
        const val SERVICE_CTR_STATUS_KEY: String = "SERVICE_CTR_STATUS_KEY"
        const val SP_NAME = "dump_sp"
    }

    private val _serviceState =  MutableStateFlow(DumpService.mAccessibilityService != null)

    val serviceState: StateFlow<Boolean> get() = _serviceState

    private val _serviceCtrlState: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(SpUtil.instance(SP_NAME).get(SERVICE_CTR_STATUS_KEY, false))
    }

    val serviceCtrlState: StateFlow<Boolean> get() = _serviceCtrlState

    suspend fun refreshServiceState() = withContext(Dispatchers.IO){
        _serviceState.emit(DumpService.mAccessibilityService != null)
        _serviceCtrlState.emit(SpUtil.instance(SP_NAME).get(SERVICE_CTR_STATUS_KEY, false))
    }

    suspend fun saveServiceCtrlState(value: Boolean) = withContext(Dispatchers.IO){
        SpUtil.instance(SP_NAME).put(SERVICE_CTR_STATUS_KEY, value)
        refreshServiceState()
    }
}