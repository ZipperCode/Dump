package com.zipper.base.service.plugin.impl

import android.content.Context
import com.zipper.base.service.IPluginAutoApi
import com.zipper.core.PluginManager

object AutoApiPao {

    const val MODULE_NAME = "Plugin_AutoApi"

    @JvmStatic
    fun fetchData(context: Context?) {
        val plugin = PluginManager.getPlugin<IPluginAutoApi>(MODULE_NAME) ?: return
        plugin.fetchData(context)
    }

}