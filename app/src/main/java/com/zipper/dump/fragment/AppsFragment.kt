package com.zipper.dump.fragment

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseVmBFragment
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.activity.AppDetailActivity
import com.zipper.dump.activity.AppsViewModel
import com.zipper.dump.adapter.AppAdapter
import com.zipper.dump.bean.AppsInfo
import com.zipper.dump.databinding.FragmentAppsBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class AppsFragment : BaseVmBFragment<AppsViewModel, FragmentAppsBinding>() {
    override fun vmBrId(): Int = BR.vm

    private lateinit var mAdapter: AppAdapter

    private val mRecyclerView: RecyclerView by ViewById(R.id.recyclerView)

    private val mApplyAllSwitchView: SwitchCompat by ViewById(R.id.sw_dump)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = AppAdapter(requireContext()) {item, action ->
            when(action){
                AppAdapter.ACTION_LONG_CLICK -> onItemLongClick(item)
                AppAdapter.ACTION_CHECKED -> onItemCheck(item)
            }
        }
        mRecyclerView.adapter = mAdapter

//        mBaseViewModel.showLoadingState.observe(viewLifecycleOwner, Observer {
//
//        })

        mBaseViewModel.appsData.observe(viewLifecycleOwner, Observer {
            mAdapter.submit(it)
        })

        mBaseViewModel.searchData.observe(viewLifecycleOwner, Observer {
            mAdapter.filter.filter(it)
        })

        mApplyAllSwitchView.setOnCheckedChangeListener { _, isChecked ->
            mBaseViewModel.applyAll(isChecked)
        }

    }

    private fun onItemLongClick(item: AppsInfo){
        val intent = Intent(requireContext(), AppDetailActivity::class.java)
        intent.putExtra("title", item.appName)
        intent.putExtra("packageName", item.pks)
        requireActivity().startActivity(intent)
    }

    private fun onItemCheck(item: AppsInfo){
        mBaseViewModel.applyItem(item)
    }

}