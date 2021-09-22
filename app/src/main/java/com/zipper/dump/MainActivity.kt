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
}