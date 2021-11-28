package com.zipper.auto.api.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.bean.ModuleTaskBean
import com.zipper.auto.api.databinding.ListItemModuleTaskBinding
import com.zipper.core.adapter.BaseBindingListAdapter
import com.zipper.core.adapter.BaseBindingViewHolder
import java.lang.Exception

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/13
 **/
class ModuleTaskAdapter(
    val onClickBtn: (type: Int, bean: ModuleTaskBean) -> Unit
) : BaseBindingListAdapter<ModuleTaskBean, ListItemModuleTaskBinding>(
    R.layout.list_item_module_task,
    ModuleTaskDiff()
) {

    override fun onBindItem(holder: BaseBindingViewHolder<ListItemModuleTaskBinding>, position: Int, bean: ModuleTaskBean) {
        holder.binding.setVariable(BR.item, bean)
        clickable(holder, bean)
        holder.binding.apply {
            btnModuleRun.setOnClickListener {
                onClickBtn.invoke(CLICK_BTN_TYPE_RUN, bean)
            }
            btnModuleBan.setOnClickListener {
                onClickBtn.invoke(CLICK_BTN_TYPE_BAN,bean)
                clickable(holder, bean)
            }

            btnModuleDel.setOnClickListener {
                onClickBtn.invoke(CLICK_BTN_TYPE_DEL, bean)
            }
        }
    }

    private fun clickable(holder: BaseBindingViewHolder<ListItemModuleTaskBinding>,bean: ModuleTaskBean){
        try {
            (holder.itemView as CardView).isClickable = !bean.moduleIsBan.get()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    companion object{
        const val CLICK_BTN_TYPE_RUN: Int = 0
        const val CLICK_BTN_TYPE_BAN: Int = 1
        const val CLICK_BTN_TYPE_DEL: Int = 2
    }
}

class ModuleTaskDiff : DiffUtil.ItemCallback<ModuleTaskBean>() {
    override fun areItemsTheSame(oldItem: ModuleTaskBean, newItem: ModuleTaskBean): Boolean {
        return oldItem.moduleKey == newItem.moduleKey
    }

    override fun areContentsTheSame(oldItem: ModuleTaskBean, newItem: ModuleTaskBean): Boolean {
        return oldItem == newItem
    }

}

class ModuleTaskViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)