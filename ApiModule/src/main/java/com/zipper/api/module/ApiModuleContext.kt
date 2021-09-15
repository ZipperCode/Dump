package com.zipper.api.module

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Resources

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/15
 **/
class ApiModuleContext(base: Context): ContextWrapper(base) {

    private var assetManager: AssetManager? = null

    private var res: Resources? = null

    private var tm: Resources.Theme? = null

    init {

    }

    override fun getSystemService(name: String): Any {
        when(name){
            Context.LAYOUT_INFLATER_SERVICE -> {
                return true
            }
            else ->{
                return super.getSystemService(name)
            }
        }
    }
}