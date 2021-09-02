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
import com.zipper.auto.api.activity.bean.TaskInfoBean

class TaskAdapter(context: Context,private val onClick: () -> Unit): ListAdapter<TaskInfoBean, TaskViewHolder>(TaskItemDiffer()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.list_item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            onClick.invoke()
        }
    }

    override fun onViewRecycled(holder: TaskViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }
}

class TaskItemDiffer: DiffUtil.ItemCallback<TaskInfoBean>(){
    override fun areContentsTheSame(oldItem: TaskInfoBean, newItem: TaskInfoBean): Boolean {
        return oldItem.title == oldItem.title
    }

    override fun areItemsTheSame(oldItem: TaskInfoBean, newItem: TaskInfoBean): Boolean {
        return oldItem == newItem
    }
}

class TaskViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root)