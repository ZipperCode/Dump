package com.zipper.auto.api.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zipper.api.module.ApiModuleManager
import com.zipper.api.module.activity.ModuleActivity
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.adapter.ModuleTaskAdapter
import com.zipper.auto.api.databinding.FragmentHomeBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.core.utils.L

class HomeFragment : BaseNavVmBFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun vmBrId(): Int = BR.vm

    private val rvList: RecyclerView by ViewById(R.id.rv_list)

    private lateinit var adapter: ModuleTaskAdapter

    override fun getVariable(): SparseArray<Any> {
        adapter = ModuleTaskAdapter() { type, bean ->
            when (type) {
                0 /* run */ -> {
                    if (ApiModuleManager.checkModuleIsRun(bean.moduleKey)) {
                        L.d(HomeViewModel.TAG, "模块 ${bean.moduleKey} 正在运行 请停止后再手动运行")
                        return@ModuleTaskAdapter
                    }
                    mBaseViewModel.customCallModule(bean.moduleKey)
                }
                1 /* ban */ -> {
                    mBaseViewModel.banModule(bean)
                }
                2 /* del */ -> {
                    mBaseViewModel.delModule(bean)
                }
                else -> {
                    val intent = Intent(requireActivity(), ModuleActivity::class.java)
                    intent.putExtra(ModuleActivity.API_MODULE_KEY, bean.moduleKey)
                    startActivity(intent)
                }
            }
        }
        return SparseArray<Any>().apply {
            put(BR.adapter, adapter)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBaseViewModel.taskInfoList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}