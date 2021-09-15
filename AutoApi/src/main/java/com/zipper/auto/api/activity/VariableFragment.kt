package com.zipper.auto.api.activity

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.adapter.VariableAdapter
import com.zipper.auto.api.activity.dialog.VariableEditDialog
import com.zipper.auto.api.databinding.FragmentVariableBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment

class VariableFragment: BaseNavVmBFragment<VariableViewModel,FragmentVariableBinding>() {
    override fun vmBrId(): Int = BR.vm

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    private val flAdd: FloatingActionButton by ViewById(R.id.fl_add)

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.adapter, VariableAdapter(requireContext()){
                VariableEditDialog.showDialog(requireActivity(), it)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flAdd.setOnClickListener {
//            VariableEditDialog.showDialog(requireActivity(), null){ resultBean ->
//                mBaseViewModel.containAndAdd(resultBean)
//            }
            navController.navigate(R.id.action_nav_variable_fragment_to_fourFragment)
        }

        mBaseViewModel.variableItemList.observe(viewLifecycleOwner, Observer {
            (rvList.adapter as VariableAdapter).submitList(it)
        })

        mBaseViewModel.requestData()
    }

}