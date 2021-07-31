package com.zipper.auto.api

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.adapter.DetailAdapter
import com.zipper.auto.api.adapter.TitleAdapter
import com.zipper.auto.api.bean.TitleBean
import com.zipper.core.activity.BaseActivity
import com.zipper.core.activity.BaseVmActivity

class ViewPointActivity : BaseVmActivity<ViewPointViewModel>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvTitle: RecyclerView
    private lateinit var totalCount: TextView

    private lateinit var mTitleAdapter: TitleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_point)
        totalCount = findViewById(R.id.total_count)
        recyclerView = findViewById(R.id.recyclerView)
        rvTitle = findViewById(R.id.rv_titles)
        recyclerView.adapter = DetailAdapter(this, mBaseViewModel.getViewPoint()) {
            startActivity(
                Intent(
                    this,
                    DetailActivity::class.java
                ).apply { putExtra(DetailActivity.ID_KEY, it) })
        }
        mTitleAdapter = TitleAdapter(this){
            if("全部" == it){
                mBaseViewModel.loadLocalLiveData()
            }else{
                mBaseViewModel.loadByExpertId(it)
            }
        }
        rvTitle.adapter = mTitleAdapter
        mBaseViewModel.getViewPoint().observe(this, Observer{
            totalCount.text = "${it.size}"
        })

        mBaseViewModel.getTitleData().observe(this, Observer {
            val list = mutableListOf(TitleBean("全部", ObservableBoolean(true)))
            list.addAll(it)
            mTitleAdapter.submit(list)
        })
    }
}