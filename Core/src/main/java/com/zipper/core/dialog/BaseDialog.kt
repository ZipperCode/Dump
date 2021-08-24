package com.zipper.core.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

/**
 *  @author zipper
 *  @date 2021-08-09
 *  @description
 **/
abstract class BaseDialog: DialogFragment(){

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId(), container, false)

    override fun onStart() {
        super.onStart()
        adjustDialog()
    }

    fun adjustDialog(){
        dialog?.apply {
            window?.apply {
                val layoutParams: WindowManager.LayoutParams = attributes
                decorView.apply {
                    setPadding(0, 0, 0, 0)
                    background = ColorDrawable(Color.TRANSPARENT)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        statusBarColor = Color.TRANSPARENT
                    } else {
                        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    }

                    // 显示区域延伸至刘海屏
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    }
                }
                attributes = layoutParams
            }
        }
    }
}