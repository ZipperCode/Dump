package com.zipper.core.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/
abstract class BaseVMFragment<VM : ViewModel> : BaseFragment() {

    private lateinit var mFragmentProvider: ViewModelProvider
    private lateinit var mActivityProvider: ViewModelProvider

    protected lateinit var mBaseViewModel: VM
        private set

    protected val mTypeArguments: Array<out Type>

    init {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        mTypeArguments = parameterizedType.actualTypeArguments
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        if (mTypeArguments.isEmpty()) {
            throw IllegalArgumentException("argument Type num < 1, must be noe type param")
        }
        val vmClass = mTypeArguments[0] as Class<VM>
        mBaseViewModel = getFragmentViewModel(vmClass)
    }

    protected open fun <T : ViewModel> getActivityViewModel(clazz: Class<T>): T {
        if (!::mActivityProvider.isInitialized) {
            mActivityProvider = ViewModelProvider(requireActivity())
        }
        return mActivityProvider.get(clazz)
    }

    protected open fun <T : ViewModel> getFragmentViewModel(clazz: Class<T>): T {
        if (!::mActivityProvider.isInitialized) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider.get(clazz)
    }

}