package com.zipper.auto.api.activity

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.adapter.TaskAdapter
import com.zipper.auto.api.databinding.FragmentHomeBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment

class HomeFragment : BaseNavVmBFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun vmBrId(): Int = BR.vm

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.adapter, TaskAdapter(requireContext()){
                navController.navigate(R.id.action_nav_home_fragment_to_oneFragment)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBaseViewModel.taskInfoList.observe(viewLifecycleOwner, Observer {
            (rvList.adapter as TaskAdapter).submitList(it)
        })

        mBaseViewModel.requestData()
    }

}