package com.zipper.auto.api.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.R
import com.zipper.auto.api.BR
import com.zipper.auto.api.bean.TitleBean

class TitleAdapter(
    private val context: Context,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val mData:MutableList<TitleBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            mLayoutInflater, R.layout.item_view_point_title, parent, false
        )
        return TitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = mData[position]
        holder.binding.apply {
            setVariable(BR.row, item)
            executePendingBindings()
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(item.title)
            mData.forEach {
                it.checked.set(false)
            }
            item.checked.set(true)
        }
    }

    fun submit(data: List<TitleBean>){
        mData.clear()
        mData.addAll(data)
        mData.first().checked.set(true)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mData.size

    class TitleViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
