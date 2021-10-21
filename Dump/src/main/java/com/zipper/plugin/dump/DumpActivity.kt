package com.zipper.plugin.dump

import com.zipper.core.activity.BaseVmBActivity
import com.zipper.plugin.dump.BR
import com.zipper.plugin.dump.databinding.ActivityDumpBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class DumpActivity: BaseVmBActivity<MainViewModel, ActivityDumpBinding>() {
    override fun vmBrId(): Int = BR.vm
}