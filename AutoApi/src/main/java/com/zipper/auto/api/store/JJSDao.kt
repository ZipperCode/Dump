package com.zipper.auto.api.store

import androidx.room.*
import com.zipper.auto.api.bean.ContentResult
import com.zipper.auto.api.bean.Expert
import com.zipper.auto.api.bean.ViewPoint
import kotlinx.coroutines.flow.Flow

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
@Dao
interface JJSDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(beans: Array<ViewPoint>): List<Long>

    @Query("""
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info 
        where status = 0 
        order by jSaleCount desc
    """)
    fun findAll(): Flow<List<ViewPoint>>

    @Query("""
        select id,title, content from tb_api_info where id = :id
    """)
    fun findContentById(id: Int): ContentResult?

}