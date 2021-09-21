package com.zipper.auto.api.activity

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.adapter.VariableAdapter
import com.zipper.auto.api.activity.bean.VariableItemBean
import com.zipper.auto.api.activity.dialog.VariableEditDialog
import com.zipper.auto.api.databinding.FragmentVariableBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment
import kotlinx.coroutines.launch

class VariableFragment: BaseNavVmBFragment<VariableViewModel,FragmentVariableBinding>() {
    override fun vmBrId(): Int = BR.vm

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    private val flAdd: FloatingActionButton by ViewById(R.id.fl_add)

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.adapter, VariableAdapter(requireContext()){
                showEditVariableDialog(it)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flAdd.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                showEditVariableDialog(null)
            }
        }

        mBaseViewModel.variableItemList.observe(viewLifecycleOwner, Observer {
            (rvList.adapter as VariableAdapter).submitList(it)
        })
    }


    private fun showEditVariableDialog(variableItemBean: VariableItemBean?){
        VariableEditDialog.showDialog(requireActivity(), variableItemBean){ resultBean ->
            if (variableItemBean == null){
                mBaseViewModel.addVariable(resultBean)
            }else{
                mBaseViewModel.updateVariable(resultBean)
            }

        }
    }

}