package com.zipper.base.service.plugin.pao

import android.content.Context
import com.zipper.base.service.plugin.IPluginAutoApi
import com.zipper.core.plugin.PluginManager

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