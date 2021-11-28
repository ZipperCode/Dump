package com.zipper.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/13
 **/
abstract class BaseBindingListAdapter<ITEM,VDB : ViewDataBinding >(
    @LayoutRes
    val itemLayout: Int,
    diff: DiffUtil.ItemCallback<ITEM>
) : ListAdapter<ITEM,BaseBindingViewHolder<VDB>>(diff) {
    protected lateinit var inflater: LayoutInflater

    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<VDB> {
        if (!::context.isInitialized){
            context = parent.context
        }
        if (!::inflater.isInitialized){
            inflater = LayoutInflater.from(context)
        }
        val binding: VDB = DataBindingUtil.inflate<VDB>(inflater, itemLayout, parent, false)
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VDB>, position: Int) {
        onBindItem(holder,position, getItem(position))
        holder.binding.executePendingBindings()
    }

    override fun onViewRecycled(holder: BaseBindingViewHolder<VDB>) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }

    override fun submitList(list: List<ITEM>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    abstract fun onBindItem(holder: BaseBindingViewHolder<VDB>, position: Int, bean: ITEM)

}

class BaseBindingViewHolder<VDB: ViewDataBinding>(val binding: VDB): RecyclerView.ViewHolder(binding.root)