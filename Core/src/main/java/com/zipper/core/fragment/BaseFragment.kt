package com.zipper.core.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    private val imm get() = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(layoutId(), container, true)
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    protected open fun showKeyboard() {
        val currentFocus = view?.findFocus()
        if (currentFocus != null) {
            imm.showSoftInput(currentFocus, 0)
        } else {
            view?.apply {
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
        view?.apply {
            val focusView = findFocus()
            if (focusView == null) {
                imm.hideSoftInputFromWindow(this.windowToken, 0)
            } else {
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
            }
        }
    }

    protected open fun showToast(msg: String, isLong: Boolean = false) {
        Toast.makeText(requireContext(), msg, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
            .show()
    }
}