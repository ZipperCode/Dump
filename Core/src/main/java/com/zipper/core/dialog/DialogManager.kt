package com.zipper.core.dialog

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference
import java.util.*

object DialogManager {

    private val mDialogStack: Deque<WeakReference<BaseDialog>> = LinkedList()

    fun dialogPush(activity: FragmentActivity, dialog: BaseDialog) {
        if (mDialogStack.isNotEmpty()) {
            // 隐藏上层的窗口
            mDialogStack.peek()?.get()?.hide(activity)
        }
        // 当前窗口入栈
        mDialogStack.push(WeakReference(dialog))
    }

    fun dialogPop(activity: FragmentActivity) {
        if (mDialogStack.isNotEmpty()) {
            mDialogStack.pop()
            mDialogStack.peek()?.get()?.hide2Show(activity)
        }
    }

}