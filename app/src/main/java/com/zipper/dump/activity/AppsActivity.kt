package com.zipper.dump.activity

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.adapter.AppsAdapter
import com.zipper.dump.databinding.ActivityAppsBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
@Deprecated("old")
class AppsActivity: BaseVmBActivity<AppsViewModel,ActivityAppsBinding>() {
    override fun vmBrId(): Int = BR.vm

    private lateinit var mAdapter: AppsAdapter

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = AppsAdapter(this, mBaseViewModel.appsData.value!!)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.adapter = mAdapter

        lifecycleScope.launchWhenCreated {
            mBaseViewModel.getPackages(this@AppsActivity)
        }
    }
}