package com.zipper.dump.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.adapter.AppInfoAdapter
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.service.DumpService
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.AppUtils
import com.zipper.dump.view.FloatWindow
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var mAppInAdapter: AppInfoAdapter

    private lateinit var mSearchEditText: EditText

    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        recyclerView = findViewById(R.id.rv_list)
        mAppInAdapter = AppInfoAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAppInAdapter


        mSearchEditText = findViewById(R.id.et_search)

        mSearchEditText.addTextChangedListener(afterTextChanged = {
            val text = it?.toString() ?: ""
            mAppInAdapter.filter.filter(text)
        })

        // 显示更快
        App.mMainCoroutinesScope.launch {
            mAppInAdapter.setData(AccessibilityHelper.mMainAppInfo)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!AppUtils.isAccessibilitySettingsOn(this, DumpService::class.java)) {
            Toast.makeText(this, "您还未开启无障碍权限", Toast.LENGTH_LONG).show()
        } else {
            mAppInAdapter.notifyDataSetChanged()
        }
    }


    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}