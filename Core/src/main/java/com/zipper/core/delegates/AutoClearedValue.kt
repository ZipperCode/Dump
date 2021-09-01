package com.zipper.core.delegates

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.zipper.core.DefaultLifecycleObserver
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 属性委托根据生命周期自动清理值
 */
class AutoClearedValue<T : Any>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {

    private var _value: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate() {
                fragment.viewLifecycleOwner.lifecycle.addObserver(object :
                    DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        _value =  null
                    }
                })
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }
}