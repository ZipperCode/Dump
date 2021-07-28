package com.zipper.core.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zipper.core.BaseApp
import java.lang.reflect.ParameterizedType

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/
abstract class BaseVmActivity<VM : ViewModel> : BaseActivity() {

    private lateinit var mActivityProvider: ViewModelProvider

    private lateinit var mAppProvider: ViewModelProvider

    protected lateinit var mBaseViewModel: VM
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 避免内存泄漏
        for (fragment in supportFragmentManager.fragments){
            supportFragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = parameterizedType.actualTypeArguments
        if (actualTypeArguments.isEmpty()) {
            throw IllegalArgumentException("argument Type num <= 1, must be noe type param")
        }
        val vmClass = actualTypeArguments[0] as Class<VM>
        mBaseViewModel = getActivityViewModel(vmClass)
    }

    protected open fun <T : ViewModel> getActivityViewModel(clazz: Class<T>): T {
        if (!::mActivityProvider.isInitialized) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider.get(clazz)
    }

    protected open fun <T : ViewModel> getAppViewModel(clazz: Class<T>): T {
        if (!::mAppProvider.isInitialized) {
            mActivityProvider =
                ViewModelProvider(application as BaseApp, (application as BaseApp).getFactory())
        }
        return mActivityProvider.get(clazz)
    }
}