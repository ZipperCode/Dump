package com.zipper.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.zipper.core.utils.L

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
interface DefaultLifecycleObserver: LifecycleObserver {

    fun onCreate(){
        L.d()
    }

    fun onStart(owner: LifecycleOwner){L.d()}

    fun onResume(owner: LifecycleOwner){L.d()}

    fun onPause(owner: LifecycleOwner){L.d()}

    fun onStop(owner: LifecycleOwner){L.d()}

    fun onDestroy(owner: LifecycleOwner){L.d()}
}