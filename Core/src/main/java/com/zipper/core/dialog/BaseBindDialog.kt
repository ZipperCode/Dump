package com.zipper.core.dialog

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindDialog<VDB: ViewDataBinding>: BaseDialog() {

    private lateinit var mBinding: VDB

    protected open fun getBindVariable(): SparseArray<Any> = SparseArray()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<VDB>(inflater, layoutId(), container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        val sparseArray = getBindVariable()

        if(sparseArray.isNotEmpty()){
            for(i in 0 until sparseArray.size()){
                mBinding.setVariable(sparseArray.keyAt(i), sparseArray.valueAt(i))
            }
        }
        mBinding.executePendingBindings()
        return mBinding.root
    }

}