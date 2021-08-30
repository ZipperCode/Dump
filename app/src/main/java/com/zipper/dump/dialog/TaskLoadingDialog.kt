package com.zipper.dump.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.zipper.core.ext.dp2px
import com.zipper.dump.R

/**
 *  @author zipper
 *  @date 2021-08-09
 *  @description
 **/
class TaskLoadingDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window!!.decorView.background = ColorDrawable(Color.TRANSPARENT)
            window!!.apply {
                val lp = attributes
                lp.dimAmount = 0f
                attributes = lp
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.common_state_loading, container, true)
    }



}