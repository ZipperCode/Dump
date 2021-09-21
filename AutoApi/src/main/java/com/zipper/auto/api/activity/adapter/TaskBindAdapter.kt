package com.zipper.auto.api.activity.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.zipper.auto.api.R

object TaskBindAdapter {

    @JvmStatic
    @BindingAdapter("banCardBackgroundColor")
    fun banCardBackgroundColor(view: CardView, isBan: Boolean) {
        view.setCardBackgroundColor(
            ResourcesCompat.getColor(
                view.resources,
                if (isBan) R.color.bg_task_item_ban else R.color.bg_task_item_normal,
                view.resources.newTheme()
            )
        )
    }

    @JvmStatic
    @BindingAdapter("banBackground")
    fun banBackground(view: View, isBan: Boolean) {
        if (isBan) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.bg_ban_task_item,
                view.resources.newTheme()
            )
        } else {
            view.background = ColorDrawable(Color.WHITE)
        }
    }

    @JvmStatic
    @BindingAdapter("banTextColor")
    fun banTextColor(view: TextView, isBan: Boolean) {
        view.setTextColor(
            ResourcesCompat.getColor(
                view.resources,
                if (isBan) R.color.task_item_ban_text_color else R.color.task_item_normal_text_color,
                view.resources.newTheme()
            )
        )
    }

    @JvmStatic
    @BindingAdapter("banBtnText")
    fun banBtnText(view: TextView, isBan: Boolean){
        view.setText(if (isBan) R.string.module_ban else R.string.module_resume)
    }

    @JvmStatic
    @BindingAdapter("banViewVisible")
    fun banViewVisible(view: View, isRun: Boolean){
        view.visibility = if(isRun) View.VISIBLE else View.GONE
    }

}