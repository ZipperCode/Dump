package com.zipper.core.delegates

import android.app.Activity
import android.view.View
import java.lang.RuntimeException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewById<T : View>(val id: Int) : ReadOnlyProperty<Any?, T> {

    private var _view: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if(thisRef == null){
            throw RuntimeException("targetRef object must not null")
        }
        if(_view == null){
            _view = (thisRef as Activity).findViewById<T>(id)
        }
        return _view ?: throw IllegalAccessException("access view obj id = $id is null")
    }
}