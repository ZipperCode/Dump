package com.zipper.dump.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zipper.dump.BR
import com.zipper.dump.BindConst
import com.zipper.dump.R
import com.zipper.dump.bean.VariableItemBean

class VariableAdapter(private val onClick: (VariableItemBean) -> Unit)
    : ListAdapter<VariableItemBean, VariableViewHolder>(VariableItemDiffer()) {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariableViewHolder {
        if (!::layoutInflater.isInitialized){
            layoutInflater = LayoutInflater.from(parent.context)
        }
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