package com.zipper.dump.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.service.DumpService
import com.zipper.dump.service.GuardService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import kotlinx.coroutines.*

class SplashActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mServiceSwitchCardView:CardView
    private lateinit var mAppsSettingCardView:CardView
    private lateinit var mSettingCardView:CardView

    private lateinit var mServiceSwitchIconView: ImageView
    private lateinit var mServiceSwitchTitleView: TextView

    private var mServiceStatus = App.serviceStatus

    override fun contentView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mServiceSwitchCardView = findViewById(R.id.cv_service_switch)
        mAppsSettingCardView = findViewById(R.id.cv_apps_setting)
        mSettingCardView = findViewById(R.id.cv_setting)

        mServiceSwitchIconView = findViewById(R.id.iv_service_switch_icon)
        mServiceSwitchTitleView = findViewById(R.id.tv_service_switch_title)

        mServiceSwitchCardView.run {
            switchServiceVisible()
            setOnClickListener(this@SplashActivity)
        }

        mAppsSettingCardView.setOnClickListener(this)
        mSettingCardView.setOnClickListener(this)

        App.mMainCoroutinesScope.launch {
            val result1 = async(Dispatchers.IO) {
                AppUtils.getLaunch(this@SplashActivity,
                    AccessibilityHelper.mMainAppInfo)
            }
            val result2 = async(Dispatchers.IO){
                AccessibilityHelper.init(this@SplashActivity)
            }
            result1.await()
            result2.await()

//            startActivity(Intent(this@SplashActivity,
//                MainActivity::class.java))
//            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.cv_service_switch ->{

                if(!AppUtils.isAccessibilitySettingsOn(this,
                        DumpService::class.java)){
                    showAccessibilityTip("检测到无障碍服务未开启，是否前往设置")
                    return
                }
                mServiceStatus = !mServiceStatus
                if(mServiceStatus){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        startForegroundService(Intent(App.mAppContext,GuardService::class.java))
                    }else{
                        startService(Intent(App.mAppContext,GuardService::class.java))
                    }
                }else{
                    stopService(Intent(App.mAppContext,GuardService::class.java))
                }
                switchServiceVisible()
            }
            R.id.cv_apps_setting ->{
                startActivity(Intent(this,MainActivity::class.java))
            }
            R.id.cv_setting -> {
                Toast.makeText(this,"打开设置", Toast.LENGTH_LONG).show()
                if (!AppUtils.isAccessibilitySettingsOn(this,
                        DumpService::class.java)) {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } else {
                    showAccessibilityTip("您当前已经拥有无障碍权限了，是否还需要跳转设置")
                }
            }
        }
    }

    private fun switchServiceVisible(){
        mServiceSwitchCardView.run {
            if(mServiceStatus){
                setCardBackgroundColor(resources.getColor(R.color.card_enable_color))
                mServiceSwitchIconView.setImageResource(R.drawable.ic_baseline_check_circle_64)
                mServiceSwitchTitleView.text =resources.getText( R.string.service_running)
            }else{
                setCardBackgroundColor(resources.getColor(R.color.card_unable_color))
                mServiceSwitchIconView.setImageResource(R.drawable.ic_baseline_cancel_64)
                mServiceSwitchTitleView.text =resources.getText( R.string.service_stopped)
            }
        }
    }

    private fun showAccessibilityTip(msg: String){
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
}