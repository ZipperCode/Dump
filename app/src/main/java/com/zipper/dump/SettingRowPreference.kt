package com.zipper.dump

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.StyleableRes
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

class SettingRowPreference : Preference {

    var mTitle: CharSequence = ""
        set(value) {
            field = value
            notifyChanged()
        }
    var mStatusText: CharSequence = ""
        set(value) {
            field = value
            notifyChanged()
        }

    var mStatusTextColor: Int = Color.RED
        set(@ColorInt value) {
            field = value
            notifyChanged()
        }

    var mUnderlineVisible: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SettingRowPreference, getAttr(
                context, R.attr.preferenceStyle,
                android.R.attr.preferenceStyle
            ), 0
        )
        mTitle = getString(a, R.styleable.SettingRowPreference_mTitle)
        mStatusText = getString(a, R.styleable.SettingRowPreference_mStatusText)
        mUnderlineVisible = a.getBoolean(R.styleable.SettingRowPreference_mUnderlineVisible,false)
        a.recycle()
    }


    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.run {
            val tvTitle: TextView = findViewById(R.id.tv_setting_title) as TextView
            val tvStatus: TextView = findViewById(R.id.tv_setting_status) as TextView
            val line: View = findViewById(R.id.v_line)
            tvTitle.text = mTitle
            tvStatus.run {
                text = mStatusText
                setTextColor(mStatusTextColor)
            }
            line.visibility = if(mUnderlineVisible) View.VISIBLE else View.GONE
        }
    }


    companion object {
        val TAG: String = SettingRowPreference::class.java.simpleName

        fun getString(@NonNull a: TypedArray, @StyleableRes index: Int): String {
            val result = a.getString(index)
            return result ?: ""
        }

        fun getAttr(context: Context, attr: Int, fallbackAttr: Int): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(attr, value, true)
            return if (value.resourceId != 0) {
                attr
            } else fallbackAttr
        }
    }
}