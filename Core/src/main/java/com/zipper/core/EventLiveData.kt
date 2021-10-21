package com.zipper.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.internal.Reflection

/**
 *  @author zipper
 *  @date 2021-07-27
 *  @description 引用KunMinX的 https://github.com/KunMinX/UnPeek-LiveData V6
 **/
class EventLiveData<T>(defaultValue: T) : LiveData<T>(defaultValue) {

    private val observerMap = ConcurrentHashMap<Observer<in T>, ObserverProxy>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val targetObserver = getObserverProxy(owner, observer)
        if(targetObserver != null){
            super.observe(owner, observer)
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        val targetObserver = getObserverProxy(null, observer)
        if(targetObserver != null){
            super.observeForever(observer)
        }
    }

    private fun getObserverProxy(owner: LifecycleOwner?, originObserver: Observer<in T>): ObserverProxy?{
        if(observerMap.containsKey(originObserver)){
            // 已经注册过了
            return null
        }
        val observerProxy = ObserverProxy(owner,originObserver)
        observerMap[originObserver] = observerProxy
        return observerProxy
    }


    override fun setValue(value: T) {
        super.setValue(value)
        if(value == null){
            return
        }

        observerMap.values.forEach { it.state = true }
        super.setValue(value)
    }

    override fun getValue(): T {
        return super.getValue()!!
    }

    override fun removeObservers(owner: LifecycleOwner) {
        super.removeObservers(owner)
        observerMap.forEach {
            if(it.value.isAttach(owner)){
                val proxy = observerMap.remove(it.key)
                if(proxy != null){
                    super.removeObserver(proxy)
                }
            }
        }
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        val proxy = observerMap.remove(observer)
        if(proxy != null){
            super.removeObserver(proxy)
        }
    }

    inner class ObserverProxy(private val owner: LifecycleOwner?, private val originObserver: Observer<in T>): Observer<T>{

        var state: Boolean = false

        fun isAttach(owner: LifecycleOwner) = this.owner == owner

        override fun onChanged(t: T) {
            val proxy = observerMap[originObserver]
            if(proxy != null && proxy.state){
                proxy.state = false
                // 只要非空数据
                if(t != null){
                    originObserver.onChanged(t)
                }
            }
        }
    }
}