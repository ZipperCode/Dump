package com.zipper.core.view

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewStub
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

class StateBindLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : StateLayout(context, attributeSet, defStyle) {

    private val viewStubProxyList: SparseArray<ViewStubProxy> = SparseArray()

    fun bindContent(bindData: SparseArray<Any>) =
        dataBind(LAYOUT_CONTENT_ID, bindData)

    fun bindError(bindData: SparseArray<Any>) =
        dataBind(LAYOUT_ERROR_ID, bindData)

    private fun dataBind(
        stateKey: Int,
        bindData: SparseArray<Any>
    ) {
        val viewStub = viewStubHolders[stateKey]
        val viewStubProxy = viewStubProxyList[stateKey]
        if(viewStubProxy == null){
            viewStubProxyList.put(stateKey, ViewStubProxy(viewStub).setBindData(bindData))
        }else{
            viewStubProxy.setBindData(bindData)
        }
    }

    fun contentLifecycle(lifecycleOwner: LifecycleOwner){
        setLifecycleOwner(LAYOUT_CONTENT_ID, lifecycleOwner)
    }

    fun errorLifecycle(lifecycleOwner: LifecycleOwner){
        setLifecycleOwner(LAYOUT_ERROR_ID, lifecycleOwner)
    }

    private fun setLifecycleOwner(stateKey: Int, lifecycleOwner: LifecycleOwner){
        val viewStub = viewStubHolders[stateKey]
        val viewStubProxy = viewStubProxyList[stateKey]
        if(viewStubProxy == null){
            viewStubProxyList.put(stateKey, ViewStubProxy(viewStub).setLifecycleOwner(lifecycleOwner))
        }else{
            viewStubProxy.setLifecycleOwner(lifecycleOwner)
        }
    }

    inner class ViewStubProxy(viewStub: ViewStub) {
        private var mViewStub: ViewStub? = viewStub
        private var mViewDataBinding: ViewDataBinding? = null

        private var mBindData: SparseArray<Any>? = null

        private val mProxyListener =
            ViewStub.OnInflateListener { _, inflated ->
                mViewStub = null
                mViewDataBinding = DataBindingUtil.bind(inflated)
                mBindData?.forEach { key, value ->
                    mViewDataBinding!!.setVariable(key, value)
                }
                mViewDataBinding!!.executePendingBindings()
            }

        init {
            mViewStub!!.setOnInflateListener(mProxyListener)
        }

        fun setBindData(bindData: SparseArray<Any>): ViewStubProxy {
            this.mBindData = bindData
            return this
        }

        fun setLifecycleOwner(owner: LifecycleOwner): ViewStubProxy{
            mViewDataBinding?.lifecycleOwner = owner
            return this
        }

    }
}