package com.zipper.auto.api.activity

import android.os.Bundle
import com.zipper.auto.api.BR
import com.zipper.auto.api.databinding.FragmentVariableBinding
import com.zipper.core.fragment.BaseNavVmBFragment

class VariableFragment: BaseNavVmBFragment<VariableViewModel,FragmentVariableBinding>() {
    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



}