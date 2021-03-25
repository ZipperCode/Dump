package com.zipper.dump.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
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

    private lateinit var flControl: FloatingActionButton

    private lateinit var searchView: SearchView

    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        recyclerView = findViewById(R.id.rv_list)
        mAppInAdapter = AppInfoAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAppInAdapter

        flControl = findViewById(R.id.fb_control)
        flControl.setOnClickListener {
            flClick()
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_act_menu, menu)
        menu?.also {
            val item = it.findItem(R.id.menu_search)
            searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    mAppInAdapter.filter.filter(newText)
                    return false
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_setting -> {
//                if (!AppUtils.isAccessibilitySettingsOn(this,
//                        DumpService::class.java)) {
//                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//                } else {
//                    showAccessibilityTip();
//                }
//            }
//            R.id.menu_help -> {
//
//            }
//            R.id.menu_exit -> {
//                finish()
//            }
//        }
        return true
    }


    private fun flClick() {
        if (FloatWindow.floatWindowIsShow) {
            FloatWindow.removeInstance(this)
        } else {
            FloatWindow.getInstance(this).setOnClickListener {
                if(AccessibilityHelper.mAccessibilityService == null){
                    toast("无障碍服务未开启，无法捕获")
                    return@setOnClickListener
                }
                toast("开启视图显示")
                AccessibilityHelper.mAccessibilityService?.run {
                    App.mIoCoroutinesScope.launch {
                        val viewInfoList: MutableList<ViewInfo> = ArrayList()
                        AccessibilityHelper.collectViewInfo(rootInActiveWindow, viewInfoList)
                        Log.d(TAG, "收集到的ViewInfo有size = ${viewInfoList.size}")
                        // 保存全局，不使用参数传递
                        AccessibilityHelper.mCollectViewInfoList = viewInfoList
                        val intent = Intent(this@MainActivity,
                            TranslucentActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }
            FloatWindow.getInstance(this).setOnLongClickListener {
                Toast.makeText(this,"长按",Toast.LENGTH_LONG).show()
                true
            }
        }
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}