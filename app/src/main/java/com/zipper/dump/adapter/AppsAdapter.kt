package com.zipper.dump.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.DiffUtil
import com.zipper.core.adapter.BaseBindingListAdapter
import com.zipper.core.adapter.BaseBindingViewHolder
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.databinding.LayoutAppsInfoBinding
import java.beans.PropertyChangeListener

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/24
 **/
class AppAdapter(context: Context,
                 private val onItemOperate: (AppsInfo, Int) -> Unit)
    : BaseBindingListAdapter<AppsInfo, LayoutAppsInfoBinding>(
    LayoutInflater.from(context),
    R.layout.layout_apps_info,
    AppsDiff()
), Filterable {

    private var originList: List<AppsInfo>? = null

    override fun onBindItem(holder: BaseBindingViewHolder<LayoutAppsInfoBinding>, position: Int, bean: AppsInfo) {
        holder.binding.setVariable(BR.rowData, bean)

        holder.itemView.setOnLongClickListener {
            onItemOperate.invoke(bean, ACTION_LONG_CLICK)
            true
        }

        bean.accessibilityEnable.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onItemOperate.invoke(bean, ACTION_CHECKED)
            }
        })
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

    companion object{
        const val ACTION_LONG_CLICK = 0
        const val ACTION_CHECKED = 1
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
