package com.zipper.dump.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.zipper.dump.R
import com.zipper.dump.utils.AccessibilityHelper
import com.zipper.dump.view.AccessibilityView

class TranslucentActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_translucent)
        val contentView: ViewGroup = window.decorView.findViewById(android.R.id.content)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        AccessibilityHelper.mCollectViewInfoList?.run {
            contentView.addView(AccessibilityView(this@TranslucentActivity, this))
        }
    }

    override fun onStart() {
        super.onStart()
        AccessibilityHelper.mDrawViewBound = true
    }

    override fun onStop() {
        super.onStop()
        AccessibilityHelper.mDrawViewBound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        AccessibilityHelper.mCollectViewInfoList?.clear()
        AccessibilityHelper.mCollectViewInfoList = null
    }
}