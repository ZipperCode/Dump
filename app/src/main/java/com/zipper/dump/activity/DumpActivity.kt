package com.zipper.dump.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.SparseArray
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.zipper.base.service.plugin.impl.AutoApiPao
import com.zipper.core.PluginManager
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.databinding.ActivityDumpBinding
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-28
 *  @description
 **/
class DumpActivity : BaseVmBActivity<DumpViewModel, ActivityDumpBinding>() {

    override fun vmBrId(): Int = BR.vm

    private lateinit var mServiceSwitchCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PluginManager.onMainActivityCreate(this)
        mServiceSwitchCardView = findViewById(R.id.cv_service_switch)
        mBaseViewModel.serviceCtrlStatus.observe(this, Observer {
            mServiceSwitchCardView.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    if (it) R.color.card_enable_color else R.color.card_unable_color,
                    resources.newTheme()
                )
            )
        })
    }

    override fun getVariable(): SparseArray<Any> {
        return SparseArray<Any>().apply {
            put(BR.handler, EventHandler())
        }
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
        AlertDialog.Builder(this)
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
            CoroutineScope(Dispatchers.Main).launch {
                if (!AppUtils.isAccessibilitySettingsOn(
                        this@DumpActivity,
                        DumpService::class.java
                    )
                ) {
                    showAccessibilityTip("检测到无障碍服务未开启，是否前往设置")
                    return@launch
                }
                mBaseViewModel.switchServiceStatus()
            }
        }

        fun openAppsSetting() {
            startActivity(Intent(this@DumpActivity, MainActivity::class.java))
        }

        fun openSetting() {
            startActivity(Intent(this@DumpActivity, SettingsActivity::class.java))
        }

        fun openHelper() {
            AutoApiPao.startListActivity()
        }
    }

}