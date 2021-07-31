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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(beans: Array<ViewPoint>): List<Long>

    @Query(
        """
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info 
        where status = 0 
        order by jSaleCount, createTime desc
    """
    )
    fun findAll(): Flow<List<ViewPoint>>

    @Query(
        """
        update tb_api_info set status = 1 where (:limitTime / 1000 - strftime("%s", createTime)) > 86400
    """
    )
    fun updateOverDue(limitTime: Long)

    @Query(
        """
        select id, expertId, type, createTime, price,jSaleCount,saleCount,status,matchTime,cid 
        from tb_api_info 
        where status = 0 and expertId = :expertId
        order by createTime desc
    """
    )
    fun findByExpertId(expertId: String): List<ViewPoint>

    @Query(
        """
        select id,title, content from tb_api_info where id = :id
    """
    )
    fun findContentById(id: Int): ContentResult?

}