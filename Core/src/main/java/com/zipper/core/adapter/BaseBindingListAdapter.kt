package com.zipper.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/13
 **/
abstract class BaseBindingListAdapter<ITEM,VDB : ViewDataBinding >(
    val inflater: LayoutInflater,
    @LayoutRes
    val itemLayout: Int,
    diff: DiffUtil.ItemCallback<ITEM>
) : ListAdapter<ITEM,BaseBindingViewHolder<VDB>>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<VDB> {
        val binding: VDB = DataBindingUtil.inflate<VDB>(inflater, itemLayout, parent, false)
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VDB>, position: Int) {
        onBindItem(holder,position, getItem(position))
        holder.binding.executePendingBindings()
    }

    abstract fun onBindItem(holder: BaseBindingViewHolder<VDB>, position: Int, bean: ITEM)

}

class BaseBindingViewHolder<VDB: ViewDataBinding>(val binding: VDB): RecyclerView.ViewHolder(binding.root)