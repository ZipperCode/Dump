package com.zipper.dump

import android.os.Bundle
import android.os.Debug
import android.os.Environment
import com.zipper.core.plugin.PluginManager
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.dump.databinding.ActivityMainBinding
import java.io.File

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class MainActivity: BaseVmBActivity<MainViewModel, ActivityMainBinding>() {
    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
//        val file = File(Environment.getExternalStorageDirectory(), "Dumping1.trace")
//        Debug.startMethodTracing(file.absolutePath)
        super.onCreate(savedInstanceState)
        PluginManager.onMainActivityCreate(this)
    }

    override fun onResume() {
        super.onResume()
//        Debug.stopMethodTracing()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

    }
}