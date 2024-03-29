package com.zipper.dump.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zipper.dump.App
import com.zipper.dump.R
import com.zipper.dump.adapter.RecycleItemTouchCallback
import com.zipper.dump.adapter.ViewInfoAdapter
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.utils.SpHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppDetailActivity : BaseActivity() {

    private lateinit var mViewInfoListView: RecyclerView

    private lateinit var mViewInfoAdapter: ViewInfoAdapter

    private val mPackageViewInfo: MutableList<ViewInfo> = ArrayList()

    private lateinit var mPackageName: String

    private lateinit var mEtDumpName: EditText
    private lateinit var mBtnSetDumpName: Button

    private var mSettingLastTime: Long = 0L

    override fun contentView(): Int = R.layout.activity_app_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }

        mViewInfoListView = findViewById(R.id.rv_view_info_list)
        mViewInfoAdapter = ViewInfoAdapter(this, mPackageViewInfo)
        mViewInfoListView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = mViewInfoAdapter
            it.addItemDecoration(DividerItemDecoration(this, 1))
            val touchHelper = ItemTouchHelper(RecycleItemTouchCallback(mViewInfoAdapter))
            touchHelper.attachToRecyclerView(it)
        }

        mBtnSetDumpName = findViewById(R.id.btn_set_dump_name)
        mEtDumpName = findViewById(R.id.et_dump_name)

        intent.extras?.apply {
            val title = getString("title", "")
            mPackageName = getString("packageName", "")

            supportActionBar?.title = "$title:$mPackageName"
            mPackageViewInfo.clear()

            App.mMainCoroutinesScope.launch {
                val dumpName = withContext(Dispatchers.IO) {
                    mPackageViewInfo.addAll(AccessibilityHelper.packageViewInfoList(mPackageName))
                    return@withContext SpHelper.loadString(mPackageName)
                }
                mEtDumpName.setText(if (TextUtils.isEmpty(dumpName)) "跳过" else dumpName)
                mViewInfoAdapter.notifyDataSetChanged()
            }
        }

        mBtnSetDumpName.setOnClickListener {
            if (TextUtils.isEmpty(mBtnSetDumpName.text)) {
                return@setOnClickListener
            }
            val nowTime = System.nanoTime()
            if (nowTime - mSettingLastTime < 2000) {
                return@setOnClickListener
            }
            mSettingLastTime = nowTime
            // 保存设置的文字
            App.mIoCoroutinesScope.launch {
                SpHelper.saveString(mPackageName, mEtDumpName.text.toString())
            }
        }
    }

    override fun onDestroy() {
        mPackageViewInfo.clear()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}