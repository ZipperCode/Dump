package com.zipper.auto.api

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.adapter.DetailAdapter

class ViewPointActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: ViewPointViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_point)
        viewModel = ViewModelProvider(this).get(ViewPointViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DetailAdapter(this,viewModel.getViewPoint()){
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}