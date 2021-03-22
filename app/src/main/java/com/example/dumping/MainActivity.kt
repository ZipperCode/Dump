package com.example.dumping

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dumping.adapter.AppInfoAdapter
import com.example.dumping.bean.AppInfo
import com.example.dumping.bean.ViewInfo
import com.example.dumping.service.DumpAccessibilityService
import com.example.dumping.utils.AccessibilityUtil
import com.example.dumping.utils.AppUtils
import com.example.dumping.utils.ThreadManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var mAppInfoList: MutableList<AppInfo> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    private lateinit var mAppInAdapter: AppInfoAdapter

    private lateinit var flControl: FloatingActionButton

    /**
     * 搜索框，用来过滤应用
     */
    private lateinit var searchView: SearchView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_list)
        flControl = findViewById(R.id.fb_control)

        mAppInAdapter = AppInfoAdapter(this, mAppInfoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAppInAdapter

        flControl.setOnClickListener {
            if(FloatWindow.floatWindowIsShow){
                FloatWindow.removeInstance(this)
            }else{
                FloatWindow.getInstance(this).setOnClickListener{
                    Toast.makeText(this,"开启视图显示",Toast.LENGTH_LONG).show()
                    AccessibilityUtil.mAccessibilityService?.run {
                        ThreadManager.runOnSub{
                            val viewInfoList: MutableList<ViewInfo> = ArrayList()
                            AccessibilityUtil.collectViewInfo(rootInActiveWindow, viewInfoList)
                            Log.d(TAG, "收集到的ViewInfo有size = ${viewInfoList.size}")

                            // 保存全局，不使用参数传递
                            AccessibilityUtil.mCollectViewInfoList = viewInfoList

                            val intent = Intent(this, TranslucentActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        // 异步处理，减小onCreate启动时间
        ThreadManager.runOnSub{
            AccessibilityUtil.init(this)
            val appList = ArrayList<AppInfo>()
            AppUtils.getLaunch(this, appList)
            mAppInAdapter.setData(appList)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!AppUtils.isAccessibilitySettingsOn(this, DumpAccessibilityService::class.java)) {
            Toast.makeText(this,"您还未开启无障碍权限",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"您已经拥有权限了",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_act_menu,menu)
        menu?.also {
            val item = it.findItem(R.id.menu_search)
            searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mAppInAdapter.filter.filter(newText)
                    return false
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_setting -> {
                if (!AppUtils.isAccessibilitySettingsOn(this, DumpAccessibilityService::class.java)) {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }else{
                    Toast.makeText(this,"您已经拥有权限了",Toast.LENGTH_LONG).show()
                    AlertDialog.Builder(this)
                        .setTitle("提示信息")
                        .setCancelable(true)
                        .setMessage("您当前已经拥有无障碍权限了，是否还需要跳转设置")
                        .setPositiveButton(
                            "确定"
                        ) { dialog, _ ->
                            dialog.dismiss()
                            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        }.setNegativeButton("取消"){ dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            R.id.menu_help ->{
                Toast.makeText(this,"Help",Toast.LENGTH_LONG).show()
            }
            R.id.menu_exit ->{
                finish()
            }
        }
        return true
    }



    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}