package com.zipper.auto.api.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zipper.auto.api.bean.ContentResult
import com.zipper.auto.api.bean.ViewPoint
import kotlinx.coroutines.flow.Flow

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
@Dao
interface JJSDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg bean: ViewPoint): List<Long>

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info
    """)
    fun findAll(): Flow<List<ViewPoint>>

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info
        where matchTime > strftime("%s", 'now')
    """)
    fun findNowDayAll(): Flow<List<ViewPoint>>

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info
        where createTime > :startTime and createTime <= :endTime
    """)
    fun find(startTime: String, endTime: String): Flow<List<ViewPoint>>

    @Query("""
        select id, content from tb_api_info where id = :id
    """)
    fun findContentById(id: Int): ContentResult?

}