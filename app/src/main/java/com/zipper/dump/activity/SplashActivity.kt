package com.zipper.dump.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import com.zipper.dump.utils.L
import com.zipper.dump.utils.SpHelper
import com.zipper.dump.view.FloatWindow
import kotlinx.coroutines.*

class SplashActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mServiceSwitchCardView: CardView
    private lateinit var mAppsSettingCardView: CardView
    private lateinit var mSettingCardView: CardView
    private lateinit var mHelpCardView: CardView

    private lateinit var mServiceSwitchIconView: ImageView
    private lateinit var mServiceSwitchTitleView: TextView

    private lateinit var flControl: FloatingActionButton

    private var mServiceStatus = false

    override fun contentView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mServiceSwitchCardView = findViewById(R.id.cv_service_switch)
        mAppsSettingCardView = findViewById(R.id.cv_apps_setting)
        mSettingCardView = findViewById(R.id.cv_setting)
        mHelpCardView = findViewById(R.id.cv_help)

        mServiceSwitchIconView = findViewById(R.id.iv_service_switch_icon)
        mServiceSwitchTitleView = findViewById(R.id.tv_service_switch_title)

        mServiceSwitchCardView.setOnClickListener(this@SplashActivity)
        mAppsSettingCardView.setOnClickListener(this)
        mSettingCardView.setOnClickListener(this)
        mHelpCardView.setOnClickListener(this)

        flControl = findViewById(R.id.fb_control)
        flControl.setOnClickListener {
            flClick()
        }

        App.mIoCoroutinesScope.launch {
            val result1 = async(Dispatchers.IO) {
                AppUtils.getLaunch(
                    this@SplashActivity,
                    AccessibilityHelper.mMainAppInfo
                )
            }
            val result2 = async(Dispatchers.IO) {
                AccessibilityHelper.init(this@SplashActivity)
            }
            result1.await()
            result2.await()
        }
    }

    override fun onResume() {
        super.onResume()
        mServiceStatus = DumpService.serviceStatus
        switchServiceVisible()

        if(!mServiceStatus and (DumpService.mAccessibilityService != null)){
            toast("?????????????????????????????????????????????")
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_service_switch -> {
                if (!AppUtils.isAccessibilitySettingsOn(
                        this,
                        DumpService::class.java
                    )
                ) {
                    showAccessibilityTip("??????????????????????????????????????????????????????")
                    return
                }
                mServiceStatus = !mServiceStatus
                DumpService.mAccessibilityService?.notifyServiceStatus(mServiceStatus)
                switchServiceVisible()
            }
            R.id.cv_apps_setting -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.cv_setting -> {
                Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,SettingsActivity::class.java))
//                if (!AppUtils.isAccessibilitySettingsOn(
//                        this,
//                        DumpService::class.java
//                    )
//                ) {
//                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//                } else {
//                    showAccessibilityTip("?????????????????????????????????????????????????????????????????????")
//                }
            }
            R.id.cv_help -> {
                Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, HelpActivity::class.java))
            }
        }
    }

    private fun flClick() {
        if (FloatWindow.floatWindowIsShow) {
            FloatWindow.removeInstance(this)
        } else {
            FloatWindow.getInstance(this).setOnClickListener {
                if (DumpService.mAccessibilityService == null) {
                    toast("???????????????????????????????????????")
                    return@setOnClickListener
                }
                toast("??????????????????")
                DumpService.mAccessibilityService?.run {
                    App.mIoCoroutinesScope.launch {
                        val viewInfoList: MutableList<ViewInfo> = ArrayList()
                        AccessibilityHelper.collectViewInfo(rootInActiveWindow, viewInfoList)
                        L.d(TAG, "????????????ViewInfo???size = ${viewInfoList.size}")
                        // ????????????????????????????????????
                        AccessibilityHelper.mCollectViewInfoList = viewInfoList
                        val intent = Intent(
                            this@SplashActivity,
                            TranslucentActivity::class.java
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }
            FloatWindow.getInstance(this).setOnLongClickListener {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_LONG).show()
                FloatWindow.removeInstance(this)
                true
            }
        }
    }

    private fun switchServiceVisible() {
        mServiceSwitchCardView.run {
            if (mServiceStatus) {
                setCardBackgroundColor(resources.getColor(R.color.card_enable_color))
                mServiceSwitchIconView.setImageResource(R.drawable.ic_baseline_check_circle_64)
                mServiceSwitchTitleView.text = resources.getText(R.string.service_running)
            } else {
                setCardBackgroundColor(resources.getColor(R.color.card_unable_color))
                mServiceSwitchIconView.setImageResource(R.drawable.ic_baseline_cancel_64)
                mServiceSwitchTitleView.text = resources.getText(R.string.service_stopped)
            }
        }
    }

    private fun showAccessibilityTip(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("????????????")
            .setCancelable(true)
            .setMessage(msg)
            .setPositiveButton(
                "??????"
            ) { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }.setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        val TAG: String = SplashActivity::class.java.simpleName
    }
}