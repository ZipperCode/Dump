package com.zipper.auto.api

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.zipper.core.activity.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailActivity: BaseActivity() {

    companion object{
        const val ID_KEY: String = "ID_KEY"
    }

    private lateinit var contentView: TextView


    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        contentView = findViewById(R.id.content)

        id = intent.extras?.getInt(ID_KEY) ?: 0

        lifecycleScope.launchWhenCreated {
            val viewPoint = withContext(Dispatchers.IO){
                PluginAutoApi.jjsDatabase.getJJSDao().findContentById(id)
            }
            if(!TextUtils.isEmpty(viewPoint?.content)){
                contentView.movementMethod = ScrollingMovementMethod.getInstance()
                contentView.text = Html.fromHtml(viewPoint!!.content)
            }
        }
    }
}