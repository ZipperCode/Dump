package com.zipper.dump.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.dump.*
import com.zipper.dump.adapter.ModuleTaskAdapter
import com.zipper.dump.bean.ModuleTaskBean
import com.zipper.dump.databinding.FragmentHomeBinding
import com.zipper.dump.fragment.base.BaseAppNavVmbFragment

class HomeFragment : BaseAppNavVmbFragment<HomeViewModel, FragmentHomeBinding>() {

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    private lateinit var adapter: ModuleTaskAdapter

    private lateinit var activityViewModel: MainViewModel

    override fun getVariable(): SparseArray<Any> {
        adapter = ModuleTaskAdapter() { type, bean ->
            when (type) {
                0 /* run */ -> {
                    mBaseViewModel.runTask(bean)
                }
                1 /* ban */ -> {
                    mBaseViewModel.banOrResumeTask(bean)
                }
                2 /* del */ -> {
                    mBaseViewModel.deleteTask(bean)
                }
                else -> {

                }
            }
        }
        return SparseArray<Any>().apply {
            put(BR.adapter, adapter)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel = getActivityViewModel(MainViewModel::class.java)
        mBaseViewModel.taskInfoList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun checkNeedServiceDialog(type: Int, bean:ModuleTaskBean){
        if (SignService.alertSignServiceDialog.value == true){
            AlertDialog.Builder(requireContext())
                .setTitle("")
                .setMessage("是否需要开启服务(开启服务后避免运行中被系统杀死)")
                .setPositiveButton("是") { dialog, _ ->
                    val intent = Intent(requireContext(), SignService::class.java)
                    intent.putExtra(SignService.INTENT_PARAM_ACTION_KEY,type)
                    intent.putExtra(SignService.INTENT_PARAM_TASK_KEY,bean.moduleKey)
                    requireContext().startService(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("否"){dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}