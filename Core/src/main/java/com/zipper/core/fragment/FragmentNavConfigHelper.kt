package com.zipper.core.fragment

import androidx.annotation.IdRes
import androidx.annotation.IntDef

object FragmentNavConfigHelper {

    const val DEFAULT = 1
    const val ONLY_TOP = DEFAULT + 1
    const val ALL = DEFAULT + 2

    @IntDef(value = [DEFAULT, ONLY_TOP, ALL])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ApplyMode

    @ApplyMode
    var navApplyMode: Int = DEFAULT
        private set

    val isDefaultMode: Boolean get() = navApplyMode == DEFAULT

    val isOnLyTopMode: Boolean get() = navApplyMode == ONLY_TOP

    val isAllMode: Boolean get() = navApplyMode == ALL

    private val topFragmentIds: MutableSet<Int> = mutableSetOf()

    fun configNavMode(@ApplyMode mode: Int): FragmentNavConfigHelper{
        navApplyMode = mode
        return this
    }

    fun addTopFragmentId(@IdRes id: Int): FragmentNavConfigHelper{
        topFragmentIds.add(id)
        return this
    }

    fun isTopLevelId(@IdRes id: Int?): Boolean = topFragmentIds.contains(id)


}