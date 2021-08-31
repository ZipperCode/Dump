package com.zipper.auto.api.activity

import android.os.Bundle
import android.view.View
import com.zipper.auto.api.BR
import com.zipper.auto.api.databinding.FragmentJdMainBinding
import com.zipper.core.fragment.BaseNavVmBFragment

class JdMainFragment : BaseNavVmBFragment<JdMainViewModel, FragmentJdMainBinding>() {

    override fun vmBrId(): Int = BR.vm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}