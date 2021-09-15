package com.zipper.auto.api.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.bean.VariableItemBean

class VariableAdapter(context: Context, private val onClick: (VariableItemBean) -> Unit)
    : ListAdapter<VariableItemBean, VariableViewHolder>(VariableItemDiffer()) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariableViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.list_item_variable, parent, false)
        return VariableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VariableViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener { onClick.invoke(getItem(position)) }
    }

    override fun onViewRecycled(holder: VariableViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }
}

class VariableItemDiffer: DiffUtil.ItemCallback<VariableItemBean>(){
    override fun areContentsTheSame(oldItem: VariableItemBean, newItem: VariableItemBean): Boolean {
        return oldItem.name.get() == oldItem.name.get()
    }

    override fun areItemsTheSame(oldItem: VariableItemBean, newItem: VariableItemBean): Boolean {
        return oldItem == newItem
    }
}

class VariableViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root)