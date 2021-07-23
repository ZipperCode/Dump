package com.zipper.dump.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.utils.AccessibilityHelper
import kotlinx.coroutines.launch

class ViewInfoAdapter(context: Context, data: MutableList<ViewInfo>)
    : RecyclerView.Adapter<ViewInfoAdapter.Companion.ViewInfoHolder>(),
    RecycleItemTouchCallback.Companion.OnItemTouchListener
{

    private val mContext: Context = context

    private val mData: MutableList<ViewInfo> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewInfoHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_view_info_item, parent, false)
        return ViewInfoHolder(view)
    }

    override fun onBindViewHolder(holder: ViewInfoHolder, position: Int) {
        holder.tvViewId.text = mData[position].viewId
        holder.tvViewRect.text = mData[position].screenRect.toString()
    }

    override fun getItemCount() = mData.size

    override fun onSwiped(position: Int) {
        val removeItem = mData.removeAt(position)
        App.mIoCoroutinesScope.launch {
            AccessibilityHelper.deleteViewInfo(removeItem)
        }
        notifyDataSetChanged()
    }

    companion object{
        class ViewInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val tvViewId: TextView = itemView.findViewById(R.id.tv_view_id)
            val tvViewRect: TextView = itemView.findViewById(R.id.tv_view_rect)
        }
    }
}