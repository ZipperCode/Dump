package com.zipper.auto.api.activity

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.adapter.TaskAdapter
import com.zipper.auto.api.activity.adapter.VariableAdapter
import com.zipper.auto.api.databinding.FragmentVariableBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment

class VariableFragment: BaseNavVmBFragment<VariableViewModel,FragmentVariableBinding>() {
    override fun vmBrId(): Int = BR.vm

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.adapter, VariableAdapter(requireContext()){

            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaseViewModel.variableList.observe(viewLifecycleOwner, Observer {
            (rvList.adapter as VariableAdapter).submitList(it)
        })

        mBaseViewModel.requestData()
    }

}