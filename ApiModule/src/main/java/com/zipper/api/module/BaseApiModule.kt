package com.zipper.api.module

import android.content.Context

abstract class BaseApiModule(val context: ApiModuleContext): IApiModule {

    protected open var debugMode:Boolean = true

    override fun debugMode(debug: Boolean) {
        this.debugMode = debug
    }

    override fun getModuleContext(): Context {
        return context
    }
}