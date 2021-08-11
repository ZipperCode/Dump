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
import android.widget.ImageView
import android.widget.LinearLayout
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

    private var animator: ObjectAnimator? = null

    private lateinit var loadingView: View

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
        loadingView = ImageView(context)
        loadingView.background =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_loading, resources.newTheme())
        val contentView = LinearLayout(requireContext())
        contentView.layoutParams =
            LinearLayout.LayoutParams(requireContext().dp2px(150), requireContext().dp2px(150))
                .apply {
                    gravity = Gravity.CENTER
                }
        contentView.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.shape_bg_loading,
            resources.newTheme()
        )
        loadingView.layoutParams =
            LinearLayout.LayoutParams(requireContext().dp2px(50), requireContext().dp2px(50))
                .apply {
                    gravity = Gravity.CENTER
                    topMargin = requireContext().dp2px(10)
                    bottomMargin = requireContext().dp2px(10)
                    leftMargin = requireContext().dp2px(10)
                    rightMargin = requireContext().dp2px(10)
                }

        contentView.addView(loadingView)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animator = ObjectAnimator.ofFloat(loadingView, "rotation", 360f, 0f).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1200
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onDestroyView() {
        animator?.cancel()
        super.onDestroyView()
    }


}