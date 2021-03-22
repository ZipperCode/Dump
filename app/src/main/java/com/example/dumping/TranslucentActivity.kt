package com.example.dumping

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.dumping.bean.ViewInfo
import com.example.dumping.utils.AccessibilityUtil

class TranslucentActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_translucent)
        val contentView: ViewGroup = window.decorView.findViewById(android.R.id.content)

        AccessibilityUtil.mDrawViewBound = true
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        AccessibilityUtil.mCollectViewInfoList?.run {
            contentView.addView(AccessibilityView(this@TranslucentActivity, this))
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        AccessibilityUtil.mCollectViewInfoList?.clear()
        AccessibilityUtil.mCollectViewInfoList = null
        AccessibilityUtil.mDrawViewBound = false
    }

}