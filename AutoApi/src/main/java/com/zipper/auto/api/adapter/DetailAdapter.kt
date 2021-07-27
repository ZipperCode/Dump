package com.zipper.auto.api.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.R
import com.zipper.auto.api.bean.ViewPoint
import com.zipper.core.format

class DetailAdapter(
    val context: Context,
    private val mData: LiveData<List<ViewPoint>>,
    val detailClick: (Int) -> Unit
    ) :
    RecyclerView.Adapter<ViewPointHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        mData.observeForever {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPointHolder {
        val view = inflater.inflate(R.layout.view_point_list_item, parent, false)
        return ViewPointHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewPointHolder, position: Int) {
        mData.value?.get(position)?.apply {
            holder.title.text = "$title"
            holder.create_time.text = "$createTime"
            holder.play_time.text = matchTime.format()
            holder.status.text = if(status == 0) "未开始" else "开始或结束"
            holder.expert.text = "$expertId"
            holder.price.text = "${price}元"
            holder.total_sale_count.text = "$jSaleCount"
            holder.sale_count.text = "$saleCount"
            holder.see_detail.setOnClickListener {
                detailClick.invoke(id)
            }
        }
    }

    override fun getItemCount(): Int = mData.value?.size ?: 0
}


class ViewPointHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val create_time: TextView = view.findViewById(R.id.create_time)
    val status: TextView = view.findViewById(R.id.status)
    val play_time: TextView = view.findViewById(R.id.play_time)
    val expert: TextView = view.findViewById(R.id.expert)
    val price: TextView = view.findViewById(R.id.price)
    val total_sale_count: TextView = view.findViewById(R.id.total_sale_count)
    val sale_count: TextView = view.findViewById(R.id.sale_count)
    val see_detail: TextView = view.findViewById(R.id.see_detail)
}