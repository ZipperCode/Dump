package com.zipper.dump.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.dump.BR
import com.zipper.dump.BindConst
import com.zipper.dump.R
import com.zipper.dump.adapter.VariableAdapter
import com.zipper.dump.bean.VariableItemBean
import com.zipper.dump.databinding.FragmentVariableBinding
import com.zipper.dump.dialog.VariableEditDialog
import com.zipper.dump.fragment.base.BaseAppNavVmbFragment
import kotlinx.coroutines.launch

class VariableFragment: BaseAppNavVmbFragment<VariableViewModel, FragmentVariableBinding>() {

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    private val flAdd: FloatingActionButton by ViewById(R.id.fl_add)

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.adapter, VariableAdapter(){
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
        VariableEditDialog.showDialog(requireActivity(),variableItemBean){
            if (variableItemBean == null){
                // save
                mBaseViewModel.addVariable(it)
            }else{
                // update
                mBaseViewModel.updateVariable(it)
            }
        }
    }

}