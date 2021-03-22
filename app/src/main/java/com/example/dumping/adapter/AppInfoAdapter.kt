package com.example.dumping.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.dumping.AppDetailActivity
import com.example.dumping.R
import com.example.dumping.bean.AppInfo
import com.example.dumping.utils.AccessibilityUtil
import com.example.dumping.utils.ThreadManager

class AppInfoAdapter(context: Context, appList: MutableList<AppInfo>):
    RecyclerView.Adapter<AppInfoAdapter.Companion.AppHolder>(),
    Filterable
{

    private var mContext: Context = context

    private var mAppList: MutableList<AppInfo> = appList

    private val mFilterList: MutableList<AppInfo> = ArrayList()

    init {
        mFilterList.addAll(mAppList)
    }

    fun setData(data: List<AppInfo>){
        mAppList.clear()
        mFilterList.clear()
        mAppList.addAll(data)
        mFilterList.addAll(mAppList)
        ThreadManager.runOnMain{ notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_app_info, parent, false)
        return AppHolder(view)
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        holder.ivAppIcon.setImageDrawable(mFilterList[position].icon)
        holder.tvAppName.text = mFilterList[position].appName
        val enableStatus = AccessibilityUtil.pksContains(mFilterList[position].pks)
        holder.swDump.isChecked = enableStatus
        holder.tvStatus.text = if(enableStatus) "Dump运行中" else "未运行"

        // 使用Click监听而不使用OnCheckChange监听是因为在划出列表后，OnCheckChange也会回调，导致错误处理
        holder.swDump.setOnClickListener { view ->
            if(view is Switch){
                holder.tvStatus.text = if(view.isChecked) "Dump运行中" else "未运行"
                if(view.isChecked){
                    AccessibilityUtil.addPks(mFilterList[position].pks )
                }else{
                    AccessibilityUtil.delPks(mFilterList[position].pks )
                }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, AppDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title",mFilterList[position].appName)
            bundle.putString("packageName",mFilterList[position].pks)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mFilterList.size
    }

    companion object{
        class AppHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var ivAppIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
            var tvAppName: TextView = itemView.findViewById(R.id.tv_app_name)

            var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
            var swDump:Switch = itemView.findViewById(R.id.sw_dump)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                mFilterList.clear()
                val str = constraint?.toString() ?: ""
                if(TextUtils.isEmpty(str)){
                    mFilterList.addAll(mAppList)
                }else{
                    for (app in mAppList){
                        if(app.appName.contains(str)){
                            mFilterList.add(app)
                        }
                    }
                }
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}

