package com.zipper.dump.fragment

import android.os.Bundle
import com.zipper.dump.databinding.FragmentPersonBinding
import com.zipper.dump.fragment.base.BaseAppNavVmbFragment

class PersonFragment: BaseAppNavVmbFragment<PersonViewModel, FragmentPersonBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}