package com.zipper.auto.api.store

import androidx.room.Dao
import com.zipper.auto.api.ViewPoint

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
@Dao
interface JJSDao {

    fun insert(bean: ViewPoint): Int

    fun findAll(): List<ViewPoint>

    fun findNowDayAll(): List<ViewPoint>

    fun finTop10Sale(): List<ViewPoint>

    fun findUnStart(): List<ViewPoint>

}