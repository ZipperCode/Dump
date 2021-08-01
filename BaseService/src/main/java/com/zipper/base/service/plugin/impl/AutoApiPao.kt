package com.zipper.base.service.plugin.impl

import android.content.Context
import com.zipper.base.service.IPluginAutoApi
import com.zipper.core.PluginManager

object AutoApiPao {

    private const val MODULE_NAME = "Plugin_AutoApi"

    @JvmStatic
    fun fetchData(context: Context? = null) {
        val plugin = PluginManager.getPlugin<IPluginAutoApi>(MODULE_NAME) ?: return
        plugin.fetchData(context)
    }

    @JvmStatic
    fun startListActivity(){
        val plugin = PluginManager.getPlugin<IPluginAutoApi>(MODULE_NAME) ?: return
        plugin.startListActivity()
    }

    @JvmStatic
    fun startJdActivity(){
        val plugin = PluginManager.getPlugin<IPluginAutoApi>(MODULE_NAME) ?: return
        plugin.startJdActivity()
    }

}