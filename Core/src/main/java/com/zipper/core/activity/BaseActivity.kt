package com.zipper.core.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import com.zipper.core.ext.statusBarAdaptive
import com.zipper.core.utils.BarUtil

/**
 *  @author zipper
 *  @date 2021-07-26
 *  @description
 **/
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val DESIGN_UI_HEIGHT_DP = 640
        const val DESIGN_UI_WIDTH_DP = 360
    }

    private var noCompatScaledDensity = 0.0f
    private var noCompatDensity = 0.0f
    private var screenOrientation = false

    private val imm get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    protected open var isFullScreen = true
        set(value) {
            if (value != field && value) {
                adjustContentScreen()
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.transparentStatusBar(this)
        noCompatScaledDensity = resources.displayMetrics.scaledDensity
        noCompatDensity = resources.displayMetrics.density
        screenOrientation =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        autoSize()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        screenOrientation = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        val fontScale = newConfig.fontScale
        if (fontScale > 0) {
            noCompatScaledDensity = resources.displayMetrics.scaledDensity
            autoSize()
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    protected open fun autoSize() {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val screenWidthPixels = displayMetrics.widthPixels
        // px = density * dp, density = dpi / 160 <==> dpi = density * 160
        val targetDensity =
            (screenWidthPixels * 1.0f) / if (screenOrientation) DESIGN_UI_HEIGHT_DP else DESIGN_UI_WIDTH_DP
        val targetScaledDensity = targetDensity * (noCompatScaledDensity / noCompatDensity)
        val targetDensityDpi = (targetDensity * 160).toInt()
        displayMetrics.apply {
            density = targetDensity
            densityDpi = targetDensityDpi
            scaledDensity = targetScaledDensity
        }
    }

    protected open fun adjustContentScreen() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

    protected open fun showKeyboard() {
        if (currentFocus != null) {
            imm.showSoftInput(currentFocus, 0)
        } else {
            window?.decorView?.apply {
                requestFocus()
                requestFocusFromTouch()
                imm.showSoftInput(this, 0)
            }
        }
    }

    protected open fun showKeyboard(focusView: View?) {
        focusView?.apply {
            if (!this.isFocused) {
                requestFocus()
                requestFocusFromTouch()
            }
            imm.showSoftInput(focusView, 0)
        }
    }

    protected open fun hideKeyboard() {
        window?.decorView?.apply {
            val focusView = findFocus()
            if (focusView == null) {
                imm.hideSoftInputFromWindow(this.windowToken, 0)
            } else {
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
            }
        }
    }

    protected open fun showToast(msg: String, isLong: Boolean = false) {
        Toast.makeText(this, msg, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}