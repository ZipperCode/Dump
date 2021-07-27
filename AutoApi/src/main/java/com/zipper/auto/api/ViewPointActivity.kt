package com.zipper.auto.api

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.adapter.DetailAdapter
import com.zipper.core.activity.BaseActivity

class ViewPointActivity : BaseActivity() {
    private lateinit var viewModel: ViewPointViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_point)
        totalCount = findViewById(R.id.total_count)
        viewModel = ViewModelProvider(this).get(ViewPointViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DetailAdapter(this, viewModel.getViewPoint()) {
            startActivity(
                Intent(
                    this,
                    DetailActivity::class.java
                ).apply { putExtra(DetailActivity.ID_KEY, it) })
        }
        viewModel.getViewPoint().observe(this, Observer{
            totalCount.text = "${it.size}"
        })
    }
}