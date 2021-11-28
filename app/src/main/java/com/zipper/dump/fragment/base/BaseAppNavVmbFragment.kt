package com.zipper.dump.fragment.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.dump.BR
import com.zipper.dump.BindConst

abstract class BaseAppNavVmbFragment<VM: ViewModel, VDB: ViewDataBinding> : BaseNavVmBFragment<VM, VDB>(){
    override fun vmBrId(): Int = BR.vm
}