package com.example.dumping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dumping.adapter.RecycleItemTouchCallback
import com.example.dumping.adapter.ViewInfoAdapter
import com.example.dumping.bean.ViewInfo
import com.example.dumping.utils.AccessibilityUtil
import com.example.dumping.utils.ThreadManager

class AppDetailActivity : AppCompatActivity() {

    private lateinit var mViewInfoListView: RecyclerView

    private lateinit var mViewInfoAdapter: ViewInfoAdapter

    private val mPackageViewInfo: MutableList<ViewInfo> = ArrayList()

    private lateinit var mPackageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
        mViewInfoListView = findViewById(R.id.rv_view_info_list)
        mViewInfoAdapter = ViewInfoAdapter(this,mPackageViewInfo)
        mViewInfoListView.layoutManager = LinearLayoutManager(this)
        mViewInfoListView.adapter = mViewInfoAdapter
        mViewInfoListView.addItemDecoration(DividerItemDecoration(this,1))

        val touchCallback = RecycleItemTouchCallback(mViewInfoAdapter)
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(mViewInfoListView)

        val bundle =  intent.extras
        if(bundle != null){
            val title = bundle.getString("title","")
            mPackageName = bundle.getString("packageName","")
            supportActionBar?.title = "$title:$mPackageName"
            mPackageViewInfo.clear()
            ThreadManager.runOnSub{
                mPackageViewInfo.addAll(AccessibilityUtil.packageViewInfoList(mPackageName))
                runOnUiThread{
                    mViewInfoAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}