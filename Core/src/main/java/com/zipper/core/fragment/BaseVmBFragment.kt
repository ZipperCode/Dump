package com.zipper.core.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
abstract class BaseVmBFragment<VM: ViewModel, VDB: ViewDataBinding>: BaseVMFragment<VM>() {

    override fun layoutId(): Int  = 0

    abstract fun vmBrId(): Int

    protected open lateinit var mBinding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(container)
        // 使livaData可观察
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.setVariable(vmBrId(), mBaseViewModel)

        val sparseArray = getVariable()

        if(sparseArray.isNotEmpty()){
            for(i in 0 until sparseArray.size()){
                mBinding.setVariable(sparseArray.keyAt(i), sparseArray.valueAt(i))
            }
        }
    }

    protected open fun getVariable(): SparseArray<Any> {
        return SparseArray()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(viewGroup: ViewGroup?, attachToRoot: Boolean = false){
        val vdmClass = mTypeArguments[1] as Class<*>
        val inflaterMethod = vdmClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        inflaterMethod.isAccessible = true
        mBinding = inflaterMethod.invoke(null,layoutInflater, viewGroup, attachToRoot) as VDB
        inflaterMethod.isAccessible = false
    }
}