package com.zipper.dump.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.zipper.base.service.plugin.impl.AutoApiPao
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.core.utils.L
import com.zipper.dump.App
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.activity.*
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.databinding.FragmentDumpBinding
import com.zipper.dump.dialog.TaskLoadingDialog
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import com.zipper.dump.view.FloatWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class DumpFragment : BaseNavVmBFragment<DumpViewModel, FragmentDumpBinding>() {
    override fun vmBrId(): Int = BR.vm

    private lateinit var mServiceSwitchCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            mBaseViewModel.refreshServiceState()
        }
    }

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.handler, EventHandler())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mServiceSwitchCardView = view.findViewById(R.id.cv_service_switch)
        mBaseViewModel.serviceCtrlStatus.observe(viewLifecycleOwner, Observer {
            mServiceSwitchCardView.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    if (it) R.color.card_enable_color else R.color.card_unable_color,
                    resources.newTheme()
                )
            )
        })
    }

    override fun onStart() {
        super.onStart()
        if (mBaseViewModel.serviceCtrlStatus.value == false
            && mBaseViewModel.serviceStatus.value == true
        ) {
            showToast("无障碍服务已打开，请开启服务吧")
        }
    }

    private fun showAccessibilityTip(msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("提示信息")
            .setCancelable(true)
            .setMessage(msg)
            .setPositiveButton(
                "确定"
            ) { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    inner class EventHandler {

        fun openOrCloseService() {
            if (!AppUtils.isAccessibilitySettingsOn(
                    requireActivity(),
                    DumpService::class.java
                )
            ) {
                showAccessibilityTip("检测到无障碍服务未开启，是否前往设置")
                return
            }
            mBaseViewModel.switchServiceStatus()
        }

        fun openAppsSetting() {
            navController.navigate(R.id.action_dumpFragment_to_appsActivity)
        }

        fun openSetting() {
//            navController.navigate(R.id.action_dumpFragment_to_settingFragment)
            navController.navigate(R.id.action_dumpFragment_to_taskLoadingDialog)
        }

        fun openHelper() {
            AutoApiPao.startJdActivity()
        }

        fun openView() {
            if (FloatWindow.floatWindowIsShow) {
                FloatWindow.removeInstance(requireContext())
            } else {
                FloatWindow.getInstance(requireContext()).setOnClickListener {
                    if (DumpService.mAccessibilityService == null) {
                        showToast("无障碍服务未开启，无法捕获")
                        return@setOnClickListener
                    }
                    showToast("开启视图显示")
                    DumpService.mAccessibilityService?.run {
                        App.mIoCoroutinesScope.launch {
                            val viewInfoList: MutableList<ViewInfo> = ArrayList()
                            AccessibilityHelper.collectViewInfo(rootInActiveWindow, viewInfoList)
                            L.d(SplashActivity.TAG, "收集到的ViewInfo有size = ${viewInfoList.size}")
                            // 保存全局，不使用参数传递
                            AccessibilityHelper.mCollectViewInfoList = viewInfoList
                            val intent = Intent(
                                requireContext(),
                                TranslucentActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
                FloatWindow.getInstance(requireContext())
                    .setOnLongClickListener {
                        showToast("长按，隐藏悬浮球")
                        FloatWindow.removeInstance(requireContext())
                        true
                    }
            }
        }
    }
}