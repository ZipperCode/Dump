package com.zipper.core.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
abstract class BaseNavVmBFragment<VM: ViewModel, VDB: ViewDataBinding>: BaseVmBFragment<VM,VDB>() {

    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }
}