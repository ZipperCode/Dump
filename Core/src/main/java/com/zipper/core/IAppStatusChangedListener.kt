package com.zipper.core

/**
 *  @author zipper
 *  @date 2021-07-27
 *  @description
 **/
interface IAppStatusChangedListener {
    fun onForeground()

    fun onBackground()
}