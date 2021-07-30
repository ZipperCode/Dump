package com.zipper.dump.fragment

import android.content.Intent
import android.provider.Settings
import android.util.SparseArray
import android.widget.Toast
import com.zipper.core.fragment.BaseNavVmBFragment
import com.zipper.dump.BR
import com.zipper.dump.databinding.FragmentSettingBinding
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AppUtils
import com.zipper.dump.view.FloatWindow

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class SettingFragment : BaseNavVmBFragment<SettingViewModel, FragmentSettingBinding>() {

    override fun vmBrId(): Int = BR.vm

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.handler, EventHandler())
        }
    }

    inner class EventHandler {

        fun onBack() {
            navController.navigateUp()
        }

        fun toAutoBootPermission() {

        }

        fun toAccessibilityPermission() {
            val serviceStatus =
                AppUtils.isAccessibilitySettingsOn(requireContext(), DumpService::class.java)
            if (!serviceStatus) {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } else {
                Toast.makeText(context, "您已经拥有权限了", Toast.LENGTH_LONG).show()
            }
        }

        fun toFloatPermission() {
            val serviceStatus = FloatWindow.checkPermission(requireContext(), false)
            if (!serviceStatus) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    FloatWindow.openFloatPermission(requireContext())
                }
            } else {
                Toast.makeText(context, "您已经拥有权限了", Toast.LENGTH_LONG).show()
            }
        }
    }
}