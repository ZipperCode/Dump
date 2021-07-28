package com.zipper.dump.repo

import com.zipper.core.utils.SpUtil
import com.zipper.dump.service.DumpService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _serviceState =  MutableStateFlow<Boolean>(false)

    val serviceState: SharedFlow<Boolean> get() = _serviceState

    private val _serviceCtrlState = MutableStateFlow(false)

    val serviceCtrlState: Flow<Boolean> get() = _serviceCtrlState

    init {
        CoroutineScope(Dispatchers.IO).launch {
            refreshServiceState()
        }
    }

    suspend fun refreshServiceState() = withContext(Dispatchers.IO){
        _serviceState.value = DumpService.mAccessibilityService != null
        _serviceCtrlState.value = SpUtil.get().get(SERVICE_CTR_STATUS_KEY, false)
    }

    suspend fun saveServiceCtrlState(value: Boolean) = withContext(Dispatchers.IO){
        SpUtil.put(SERVICE_CTR_STATUS_KEY, value)
        refreshServiceState()
    }
}