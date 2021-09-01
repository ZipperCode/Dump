package com.zipper.auto.api.activity

import android.os.Bundle
import com.zipper.auto.api.BR
import com.zipper.auto.api.databinding.FragmentPersonBinding
import com.zipper.core.fragment.BaseNavVmBFragment

class PersonFragment: BaseNavVmBFragment<PersonViewModel, FragmentPersonBinding>() {
    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}