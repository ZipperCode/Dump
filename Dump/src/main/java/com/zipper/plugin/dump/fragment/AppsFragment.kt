package com.zipper.plugin.dump.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseVmBFragment
import com.zipper.plugin.dump.BR
import com.zipper.plugin.dump.adapter.AppAdapter
import com.zipper.plugin.dump.R
import com.zipper.plugin.dump.databinding.FragmentAppsBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsFragment : BaseVmBFragment<AppsViewModel, FragmentAppsBinding>() {
    override fun vmBrId(): Int = BR.vm

    private lateinit var mAdapter: AppAdapter

    private val mRecyclerView: RecyclerView by ViewById(R.id.recyclerView)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = AppAdapter()
        mRecyclerView.adapter = mAdapter

        mBaseViewModel.appsData.observe(viewLifecycleOwner, Observer {
            mAdapter.submit(it)
        })

        mBaseViewModel.searchData.observe(viewLifecycleOwner, Observer {
            mAdapter.filter.filter(it)
        })

        mBaseViewModel.applyAll.observe(viewLifecycleOwner, Observer {
            showToast("$it")
        })
    }

}