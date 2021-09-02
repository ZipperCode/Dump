package com.zipper.core.delegates

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.RuntimeException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewById<T : View> (val id: Int) : ReadOnlyProperty<Any?, T> {

    private var _view: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if(thisRef == null){
            throw RuntimeException("targetRef object must not null")
        }
        if(_view == null){
            if(thisRef is Fragment){
                _view = thisRef.view?.findViewById(id)
            }else if(thisRef is Activity){
                _view = thisRef.findViewById(id)
            }else if(thisRef is View){
                _view = thisRef.findViewById(id)
            }
        }
        return _view ?: throw IllegalAccessException("access view obj id = $id is null")
    }
}
