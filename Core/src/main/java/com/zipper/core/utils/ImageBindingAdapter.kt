package com.zipper.core.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("src")
    public fun setSrc(view: ImageView, bitmap: Bitmap) {
        view.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("src")
    public fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("src")
    public fun setSrc(view: ImageView, drawable: Drawable) {
        view.setImageDrawable(drawable)
    }
}