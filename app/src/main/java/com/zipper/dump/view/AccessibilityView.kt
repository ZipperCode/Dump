package com.zipper.dump.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Toast
import com.zipper.dump.App
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.room.DBHelper
import com.zipper.dump.utils.L
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class AccessibilityView(context: Context, viewRectList: List<ViewInfo>) : View(context) {

    private val mViewRectList: List<ViewInfo> = viewRectList

    private var mClickViewViewInfo: ViewInfo? = null

    private val mPaint: Paint = Paint()
    private val mClickPaint: Paint = Paint()

    private var mLongClick = false

    private var mLastClickTime = 0L

    private var mUp = false

    private var mScaledDoubleTapSlop = 0

    private val mLongClickRunnable = Runnable {
        if (!mUp) {
            mLongClick = true
        }
    }

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.GREEN

        mClickPaint.style = Paint.Style.STROKE
        mClickPaint.color = Color.GREEN
        mClickPaint.strokeWidth = 5F
        mScaledDoubleTapSlop = ViewConfiguration.get(context).scaledDoubleTapSlop
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mViewRectList.isNotEmpty()) {
            for (viewInfo in mViewRectList) {
                canvas?.drawRect(viewInfo.screenRect, mPaint)
            }
        }

        mClickViewViewInfo?.run {
            canvas?.drawRect(screenRect, mClickPaint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mScaledDoubleTapSlop == 0) {
            mScaledDoubleTapSlop = ViewConfiguration.get(context).scaledDoubleTapSlop
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                L.d(TAG, "ACTION_DOWN")
                mUp = false
                val rect = matchRect(Point(event.x.toInt(), event.y.toInt()))
                rect?.run {
                    mClickViewViewInfo = this
                    postInvalidate()
                }
                val time = System.currentTimeMillis() - mLastClickTime
                if (time < mScaledDoubleTapSlop) {

                    val viewId = mClickViewViewInfo?.viewId
                    Toast.makeText(context, "选中当前视图id为 = $viewId", Toast.LENGTH_LONG).show()
                    mClickViewViewInfo?.let {
                        AlertDialog.Builder(context)
                            .setMessage("选中当前视图id为 = $viewId,确定保存选中吗")
                            .setPositiveButton("确定") { dialog, _ ->
                                App.mIoCoroutinesScope.launch {
                                    val list = DBHelper.getViewInfoDao()
                                        .queryByViewIdAndPackageName(viewId ?: "", it.packageName)
                                    if (list.isEmpty()) {
                                        DBHelper.getViewInfoDao().insert(it)
                                    } else {
                                        DBHelper.getViewInfoDao().update(it)
                                    }
                                }
                                Toast.makeText(context, "已保存", Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                L.d(TAG, "ACTION_UP")
                mUp = true
                mLastClickTime = System.currentTimeMillis()
                return true
            }
        }
        return true
    }


    private fun matchRect(point: Point): ViewInfo? {
        val pointRect = Rect(point.x, point.y, point.x, point.y)
        var result: ViewInfo? = null
        for (viewInfo in mViewRectList) {
            val rect = viewInfo.screenRect
            if (rect.contains(pointRect)) {
                if (result == null) {
                    result = viewInfo
                } else {
                    if (!rect.contains(result.screenRect)) {
                        result = viewInfo
                    }
                }
            }
        }
        return result
    }

    companion object {
        private val TAG: String = AccessibilityView::class.java.simpleName
    }

}