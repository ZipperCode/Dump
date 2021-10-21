package com.zipper.plugin.dump.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import com.zipper.core.adapter.BaseBindingListAdapter
import com.zipper.core.adapter.BaseBindingViewHolder
import com.zipper.plugin.dump.BR
import com.zipper.plugin.dump.R
import com.zipper.plugin.dump.bean.AppsInfo
import com.zipper.plugin.dump.databinding.ListItemAppsInfoBinding

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/24
 **/
class AppAdapter(context: Context)
    : BaseBindingListAdapter<AppsInfo, ListItemAppsInfoBinding>(
    LayoutInflater.from(context),
    R.layout.list_item_apps_info,
    AppsDiff()
), Filterable {

    private var originList: List<AppsInfo>? = null

    override fun onBindItem(holder: BaseBindingViewHolder<ListItemAppsInfoBinding>, position: Int, bean: AppsInfo) {
        holder.binding.setVariable(BR.rowData, getItem(position))
    }

    fun submit(list: List<AppsInfo>?, fromOrigin: Boolean = true){
        if (fromOrigin){
            originList = list
        }
        super.submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val str = constraint?.toString() ?: ""
                if (TextUtils.isEmpty(str)) {
                    submit(originList, false)
                } else {
                    val newList = mutableListOf<AppsInfo>()
                    originList?.forEach {
                        if (it.appName.contains(str)) {
                            newList.add(it)
                        }
                    }
                    submit(newList,false)
                }
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
    }

}

class AppsDiff : DiffUtil.ItemCallback<AppsInfo>() {
    override fun areItemsTheSame(oldItem: AppsInfo, newItem: AppsInfo): Boolean {
        return oldItem.pks == newItem.pks
    }

    override fun areContentsTheSame(oldItem: AppsInfo, newItem: AppsInfo): Boolean {
        return oldItem == newItem
    }

}
