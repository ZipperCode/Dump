package com.zipper.dump.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.utils.L
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.activity.AppDetailActivity
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsAdapter(
    private val mContext: Context,
    private val mData: MutableList<AppsInfo>
) : RecyclerView.Adapter<AppsAdapter.AppsViewHolder>(), Filterable {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    private val mFilterList = mutableListOf<AppsInfo>()

    private val mAllowAllObservable: ObservableBoolean = ObservableBoolean(false)

    private val mFilterPks: List<String> get() = mFilterList.map { it.pks }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        val layoutId = ItemType.getLayoutId(viewType)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            mLayoutInflater, layoutId, parent, false
        )
        return AppsViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        when (holder.viewType) {
            ItemType.HEADER.type -> {
                holder.binding.setVariable(
                    BR.serviceStatus, AppUtils.isAccessibilitySettingsOn(
                        mContext,
                        DumpService::class.java
                    )
                )
                holder.binding.setVariable(BR.allowStatus, mAllowAllObservable)
                mAllowAllObservable.addOnPropertyChangedCallback(object :
                    Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        sender as ObservableBoolean
                        CoroutineScope(Dispatchers.Main).launch {
                            if (sender.get()) {
                                AccessibilityHelper.addPks(mFilterPks)
                            } else {
                                AccessibilityHelper.clearPks()
                            }
                        }
                    }
                })
            }
            ItemType.CONTENT.type -> {
                holder.binding.setVariable(BR.rowData, mFilterList[position])
                mFilterList[position].accessibilityEnable.addOnPropertyChangedCallback(
                    object : Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                            sender as ObservableBoolean
                            CoroutineScope(Dispatchers.Main).launch {
                                if (sender.get()) {
                                    AccessibilityHelper.addPks(mFilterList[position].pks)
                                } else {
                                    AccessibilityHelper.delPks(mFilterList[position].pks)
                                }
                                mAllowAllObservable.set(
                                    AccessibilityHelper.pksContainsAll(
                                        mFilterPks
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, AppDetailActivity::class.java)
            intent.putExtra("title", mFilterList[position].appName)
            intent.putExtra("packageName", mFilterList[position].pks)
            mContext.startActivity(intent)
        }
        if (!holder.binding.hasPendingBindings()) {
            holder.binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return mFilterList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ItemType.HEADER.type else ItemType.CONTENT.type
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                mFilterList.clear()
                val str = constraint?.toString() ?: ""
                if (TextUtils.isEmpty(str)) {
                    mFilterList.addAll(mData)
                } else {
                    for (app in mData) {
                        if (app.appName.contains(str)) {
                            mFilterList.add(app)
                        }
                    }
                }
                return FilterResults()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }

    fun submitData(newData: List<AppsInfo>){
        mFilterList.clear()
        mData.clear()
        mData.addAll(newData)
        mFilterList.addAll(mData)
    }

    class AppsViewHolder(val binding: ViewDataBinding, val viewType: Int) :
        RecyclerView.ViewHolder(binding.root)

    enum class ItemType(@LayoutRes val layoutId: Int, val type: Int) {
        HEADER(R.layout.layout_apps_info_header, 0),
        CONTENT(R.layout.layout_apps_info, 1);

        companion object {

            @LayoutRes
            fun getLayoutId(viewType: Int): Int {
                return when (viewType) {
                    HEADER.type -> HEADER.layoutId
                    CONTENT.type -> CONTENT.layoutId
                    else -> throw IllegalArgumentException("unknown viewType")
                }
            }
        }
    }

}