package com.zipper.core.eventbus.flow

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.zipper.core.BaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 *  @author zipper
 *  @date 2021-08-10
 *  @description
 **/
class EventBusFlow: ViewModel() {

    private val eventFlow: MutableMap<String, MutableSharedFlow<Any>> = mutableMapOf()

    private val stickyEventFlow:MutableMap<String, MutableSharedFlow<Any>> = mutableMapOf()

    fun getEventFlow(eventName: String, isSticky: Boolean = false): MutableSharedFlow<Any>{
        return if (isSticky) {
            stickyEventFlow[eventName]
        } else {
            eventFlow[eventName]
        } ?: MutableSharedFlow<Any>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        ).also {
            // 不存在事件就初始化
            if (isSticky) {
                stickyEventFlow[eventName] = it
            } else {
                eventFlow[eventName] = it
            }
        }
    }

    fun postEvent(eventName: String, value: Any, isSticky: Boolean = false, delayMillis:Long = 0L){
        viewModelScope.launch {
            delay(delayMillis)
            getEventFlow(eventName,isSticky).emit(value)
        }
    }

}


inline fun <reified T> observeEvent(
    lifecycleOwner: LifecycleOwner,
    eventName: String = T::class.java.name,
    isSticky: Boolean = false,
    noinline onReceive: (T) -> Unit
){
    lifecycleOwner.lifecycleScope.launch {
        BaseApp.instance.getAppViewModel(EventBusFlow::class.java)
            .getEventFlow(eventName, isSticky)
            .collect {
                try{
                    onReceive.invoke(it as T)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
    }
}

inline fun <reified T> observeEvent(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    eventName: String = T::class.java.name,
    isSticky: Boolean = false,
    noinline onReceive: (T) -> Unit
){
    coroutineScope.launch {
        BaseApp.instance.getAppViewModel(EventBusFlow::class.java)
            .getEventFlow(eventName, isSticky)
            .collect {
                try{
                    onReceive.invoke(it as T)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
    }
}