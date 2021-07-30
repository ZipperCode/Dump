package com.zipper.dump

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.zipper.core.PluginManager
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.dump.databinding.ActivityMainBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class MainActivity: BaseVmBActivity<MainViewModel, ActivityMainBinding>() {
    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PluginManager.onMainActivityCreate(this)
    }
}