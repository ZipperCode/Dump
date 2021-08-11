package com.zipper.dump.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zipper.core.ext.statusBarAdaptive
import com.zipper.dump.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if(Build.VERSION.SDK_INT > 23){
            window.decorView.statusBarAdaptive(true)
        }

        setContentView(R.layout.activity_test)

        val content = findViewById<View>(R.id.layout_test)

        val main = findViewById<View>(R.id.ll_main)

    }
}