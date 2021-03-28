package com.zipper.dump.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.zipper.dump.R
import com.zipper.dump.SettingRowPreference
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import com.zipper.dump.view.FloatWindow

class SettingsActivity : BaseActivity() {
    override fun contentView(): Int = R.layout.settings_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var mAccessibilityPreference: SettingRowPreference? = null
        private var mFloatPreference: SettingRowPreference? = null

        override fun onResume() {
            super.onResume()
            refreshStatus()
        }

        fun refreshStatus() {
            mAccessibilityPreference?.run {
                val serviceStatus =
                    AppUtils.isAccessibilitySettingsOn(context, DumpService::class.java)
                mStatusText =
                    resources.getString(
                        if (serviceStatus)
                            R.string.setting_permission_status_enable
                        else R.string.setting_permission_status_unable
                    )
                mStatusTextColor = resources.getColor(
                    if (serviceStatus)
                        R.color.text_success
                    else R.color.text_failure
                )
            }
            mFloatPreference?.run {
                val serviceStatus = FloatWindow.checkPermission(context, false)
                mStatusText =
                    resources.getString(
                        if (serviceStatus)
                            R.string.setting_permission_status_enable
                        else R.string.setting_permission_status_unable
                    )
                mStatusTextColor = resources.getColor(
                    if (serviceStatus)
                        R.color.text_success
                    else R.color.text_failure
                )
            }
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            mAccessibilityPreference = findPreference("setting_accessibility")
            mAccessibilityPreference?.run {
                val serviceStatus =
                    AppUtils.isAccessibilitySettingsOn(context, DumpService::class.java)
                setOnPreferenceClickListener {
                    return@setOnPreferenceClickListener if (!serviceStatus) {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        true
                    } else {
                        Toast.makeText(context, "您已经拥有权限了", Toast.LENGTH_LONG).show()
                        false
                    }
                }
            }
            mFloatPreference = findPreference("setting_float")

            mFloatPreference?.run {
                val serviceStatus = FloatWindow.checkPermission(context, false)
                setOnPreferenceClickListener {
                    return@setOnPreferenceClickListener if (!serviceStatus) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            FloatWindow.openFloatPermission(context)
                        }
                        true
                    } else {
                        Toast.makeText(context, "您已经拥有权限了", Toast.LENGTH_LONG).show()
                        false
                    }
                }
            }
            refreshStatus()

            val wxSwitchPreferenceCompat: SwitchPreferenceCompat? = findPreference("setting_weixin")
            wxSwitchPreferenceCompat?.run {
                setOnPreferenceChangeListener { _, newValue ->
                    if(newValue is Boolean){
                        AccessibilityHelper.mWxSettingValue = newValue
                    }
                    true
                }
            }
        }
    }

    companion object {

    }
}