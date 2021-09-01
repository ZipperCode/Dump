package com.zipper.core.view

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zipper.core.R
import java.lang.ref.WeakReference

/**
 *  @author zipper
 *  @date 2021-08-18
 *  @description
 **/
class StateLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    companion object {
        /**
         * Loading布局
         */
        const val LAYOUT_LOADING_ID = 1

        /**
         * 内容布局
         */
        const val LAYOUT_CONTENT_ID = 2

        /**
         * 空数据布局
         */
        const val LAYOUT_EMPTY_DATA_ID = 3

        /**
         * 错误视图布局
         */
        const val LAYOUT_ERROR_ID = 4
    }

    @IntDef(value = [LAYOUT_LOADING_ID, LAYOUT_CONTENT_ID, LAYOUT_EMPTY_DATA_ID, LAYOUT_ERROR_ID])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ViewState {}

    private val useDataBinding: Boolean

    private val inflateViewIds: SparseArray<Int> = SparseArray()

    private val viewStubHolders: SparseArray<ViewStub> = SparseArray()

    private val stubInflateViewHolders: SparseArray<WeakReference<View>> = SparseArray()

    private val stateViewBindData: SparseArray<SparseArray<Any>> = SparseArray()

    @ViewState
    private var currentViewState: Int = LAYOUT_LOADING_ID

    init {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.StateLayout)
        useDataBinding = typeArray.getBoolean(R.styleable.StateLayout_use_data_binding, false)

        val loadingLayoutId = typeArray.getResourceId(
            R.styleable.StateLayout_loading_layout,
            R.layout.common_state_loading
        )
        val contentLayoutId = typeArray.getResourceId(R.styleable.StateLayout_content_layout, 0)
        val emptyLayoutId = typeArray.getResourceId(
            R.styleable.StateLayout_empty_layout,
            R.layout.common_state_empty
        )
        val errorLayoutId = typeArray.getResourceId(
            R.styleable.StateLayout_error_layout,
            R.layout.common_state_error
        )

        typeArray.recycle()

        if (contentLayoutId == 0) {
            throw IllegalArgumentException("state layout must contain a content view")
        }

        initInflateIds()
        initViewStub(contentLayoutId, LAYOUT_CONTENT_ID, R.id.common_state_content_view_stub_id)
        initViewStub(loadingLayoutId, LAYOUT_LOADING_ID, R.id.common_state_loading_view_stub_id)
        initViewStub(emptyLayoutId, LAYOUT_EMPTY_DATA_ID, R.id.common_state_empty_view_stub_id)
        initViewStub(errorLayoutId, LAYOUT_ERROR_ID, R.id.common_state_error_view_stub_id)

        initInflateListener()

        viewStubHolders[currentViewState].inflate()
    }

    private fun initInflateIds() {
        inflateViewIds.put(LAYOUT_LOADING_ID, R.id.common_state_loading_view_inflate_id)
        inflateViewIds.put(LAYOUT_CONTENT_ID, R.id.common_state_content_view_inflate_id)
        inflateViewIds.put(LAYOUT_EMPTY_DATA_ID, R.id.common_state_empty_view_inflate_id)
        inflateViewIds.put(LAYOUT_ERROR_ID, R.id.common_state_error_view_inflate_id)
    }

    private fun initViewStub(layoutId: Int, @ViewState state: Int, @IdRes stubViewId: Int){
        val viewStub = ViewStub(context, layoutId)
        viewStub.id = stubViewId
        viewStub.inflatedId = inflateViewIds[state]
        viewStubHolders.put(state, viewStub)
        addView(
            viewStub,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.CENTER
            })
    }

    private fun initInflateListener(){
        if(useDataBinding){
            viewStubHolders.forEach { key, viewStub ->
                viewStub.setOnInflateListener { _, inflatedView ->
                    stubInflateViewHolders.put(key, WeakReference(inflatedView))
                    bindView(key, inflatedView)
                }
            }
        }
    }

    fun changeViewState(@ViewState state: Int) {
        if (state == currentViewState) {
            return
        }

        val currentViewStub = viewStubHolders[currentViewState]
        val newViewStub = viewStubHolders[state]

        // 隐藏显示ViewStub
        if (currentViewStub.parent == null) {
            currentViewStub.visibility = View.GONE
        }

        if (newViewStub != null) {
            currentViewState = state
            if (newViewStub.parent == null) {
                newViewStub.visibility = View.VISIBLE
            } else {
                newViewStub.inflate()
            }
        }
    }

    fun showLoading() {
        changeViewState(LAYOUT_LOADING_ID)
    }

    fun showContent() {
        changeViewState(LAYOUT_CONTENT_ID)
    }

    fun showEmptyData() {
        changeViewState(LAYOUT_EMPTY_DATA_ID)
    }

    fun showError() {
        changeViewState(LAYOUT_ERROR_ID)
    }

    private fun bindView(state: Int, inflatedView: View){
        val bindingData = stateViewBindData[state]
        if(bindingData != null){
            // 说明在inflate 前已经赋值了绑定数据，此处进行绑定
            val binding = DataBindingUtil.bind<ViewDataBinding>(inflatedView)
            binding?.run {
                bindingData.forEach { key, value ->
                    setVariable(key, value)
                }
            }
            // 绑定后移除绑定数据，避免内存泄漏
            stateViewBindData.remove(state)
        }
    }

    @IdRes
    fun getInflateId(@ViewState state: Int): Int = inflateViewIds[state]

    fun bindContent(bindData: SparseArray<Any>) = dataBind(LAYOUT_CONTENT_ID, bindData)

    fun bindError(bindData: SparseArray<Any>) = dataBind(LAYOUT_ERROR_ID, bindData)

    private fun dataBind(state: Int, bindData: SparseArray<Any>){
        if(!useDataBinding){
            return
        }
        val inflateViewRef = stubInflateViewHolders[state]
        stateViewBindData.put(state, bindData)
        if(inflateViewRef == null){
            // 说明该状态的view还没有inflate出来, 先保存绑定数据
            return
        }
        val inflateView = inflateViewRef.get()
            ?: // 说明view的引用消失了，该view已经不存在了
            throw IllegalAccessException("inflate view ref null")

        bindView(state, inflateView)

    }
}