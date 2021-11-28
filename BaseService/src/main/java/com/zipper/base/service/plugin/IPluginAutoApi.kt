package com.zipper.base.service.plugin

import android.content.Context

interface IPluginAutoApi {

    fun fetchData(context: Context? = null)

    fun startListActivity()

    fun startJdActivity()
}