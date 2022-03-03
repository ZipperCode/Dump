package com.zipper.dump.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.fragment.app.Fragment
import java.lang.Exception

/**
 *
 * @author zhangzhipeng
 * @date   2022/3/3
 **/
abstract class BaseLazyFragment : Fragment(), AsyncLayoutInflater.OnInflateFinishedListener {

    private lateinit var mContainerView: ViewGroup

    abstract fun lazyResId(): Int

    protected open fun generateContainerView(): ViewGroup{
        return FrameLayout(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::mContainerView.isInitialized){
            mContainerView = generateContainerView()
        }
        val resId = lazyResId()
        if (resId != 0){
            AsyncLayoutInflater(requireContext()).inflate(resId, mContainerView, this)
        }
        mContainerView.tag = savedInstanceState
        onViewInflateBefore(mContainerView)
        return mContainerView
    }

    @Deprecated("重写", ReplaceWith("super.onViewLazyCreated(view, savedInstanceState)", "com.zipper.dump.fragment.base.BaseLazyFragment"))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 在View完全展示出来之前需要做的状态
     */
    @MainThread
    protected open fun onViewInflateBefore(container: ViewGroup){
        
    }

    /**
     * 初始化数据，不能操作ui
     * 添加工作线程注解，防止操作ui
     */
    @WorkerThread
    protected open fun initData(){
        
    }
    
    /**
     * view 构建好之后
     */
    @MainThread
    protected open fun onViewLazyCreated(view: View, savedInstanceState: Bundle?){

    }

    @MainThread
    override fun onInflateFinished(view: View, resid: Int, parent: ViewGroup?) {
        var savedInstanceState: Bundle? = null
        if (mContainerView.tag != null && mContainerView.tag is Bundle?){
            try {
                savedInstanceState = mContainerView.tag as? Bundle
                mContainerView.tag = null
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        onViewLazyCreated(view, savedInstanceState)
    }



}