package com.zipper.auto.api.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zipper.auto.api.ViewPoint

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
@Dao
interface JJSDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg bean: ViewPoint): Int

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info
    """)
    fun findAll(): List<ViewPoint>

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info
        where matchTime > strftime("%s", 'now')
    """)
    fun findNowDayAll(): List<ViewPoint>

    @Query("""
        select id, content from tb_api_info where id = :id
    """)
    fun findById(id: Int): ViewPoint

}