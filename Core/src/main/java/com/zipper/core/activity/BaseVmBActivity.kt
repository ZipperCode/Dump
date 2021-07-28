package com.zipper.core.activity

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
 *  @date 2021-07-28
 *  @description
 **/
abstract class BaseVmBActivity<VM: ViewModel, VDB: ViewDataBinding>: BaseVmActivity<VM>() {
    private lateinit var mBinding: VDB

    abstract fun vmBrId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        // 使livaData可观察
        mBinding.lifecycleOwner = this
        mBinding.setVariable(vmBrId(), mBaseViewModel)

        val sparseArray = getVariable()

        if(sparseArray.isNotEmpty()){
            for(i in 0..sparseArray.size()){
                mBinding.setVariable(sparseArray.keyAt(i), sparseArray.valueAt(i))
            }
        }
        mBinding.executePendingBindings()
    }

    override fun <T : View?> findViewById(id: Int): T {
        return mBinding.root.findViewById<T>(id)
    }

    protected open fun getVariable(): SparseArray<Any>{
        return SparseArray()
    }

    override fun onDestroy() {
        if(::mBinding.isInitialized){
            mBinding.unbind()
        }
        super.onDestroy()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(){
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val vdmClass = parameterizedType.actualTypeArguments[1] as Class<*>
        val inflaterMethod = vdmClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java
        )
        inflaterMethod.isAccessible = true
        mBinding = inflaterMethod.invoke(null,layoutInflater) as VDB
        inflaterMethod.isAccessible = false
    }

}