package com.zipper.core.utils

import android.view.View
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("onThrottleClick")
    fun setOnclick(view: View, listener: View.OnClickListener){
        view.setOnClickListener(ThrottleOnClickListener(listener))
    }

    class ThrottleOnClickListener(private val originClickListener: View.OnClickListener): View.OnClickListener{

        private var lastClickTime  = 0L

        override fun onClick(v: View?) {
            L.d("ThrottleOnClickListener.onClick")
            val currentTime = System.currentTimeMillis()
            if(currentTime - lastClickTime > 1000L){
                L.d("ThrottleOnClickListener.onClick - click")
                lastClickTime = currentTime
                originClickListener.onClick(v)
            }
        }
    }
}