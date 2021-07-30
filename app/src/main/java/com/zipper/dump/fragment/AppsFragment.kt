package com.zipper.dump.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.fragment.BaseVmBFragment
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.activity.AppsViewModel
import com.zipper.dump.adapter.AppsAdapter
import com.zipper.dump.databinding.FragmentAppsBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsFragment : BaseVmBFragment<AppsViewModel, FragmentAppsBinding>() {
    override fun vmBrId(): Int = BR.vm

    private lateinit var mAdapter: AppsAdapter

    private lateinit var mRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = AppsAdapter(requireContext(), mBaseViewModel.appsData)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        mRecyclerView.adapter = mAdapter

        lifecycleScope.launchWhenCreated {
            mBaseViewModel.getPackages(requireActivity())
        }
    }

}