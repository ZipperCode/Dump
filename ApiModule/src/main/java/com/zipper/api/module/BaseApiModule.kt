package com.zipper.api.module

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseApiModule(val context: ApiModuleContext): IApiModule {

    companion object{
        val API_MODULE_CONTAINER_ID = R.id.module_container

        var instance: BaseApiModule? = null

        @JvmStatic
        inline fun<reified T:BaseApiModule> instance(): T?{
            return instance as? T
        }
    }

    init {
        instance = this
    }

    protected open var debugMode:Boolean = true

    protected val modulePageClassImpl: MutableList<Class<Fragment>> = mutableListOf()

    override fun debugMode(debug: Boolean) {
        this.debugMode = debug
    }

    override fun getModuleContext(): Context {
        return context
    }

    override fun stop() {}

    override fun release() {
        instance = null
    }
}