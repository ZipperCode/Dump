package com.zipper.auto.api.store

import androidx.room.*
import com.zipper.auto.api.bean.Expert
import com.zipper.auto.api.bean.ViewPoint
import kotlinx.coroutines.flow.Flow

/**
 *  @author zipper
 *  @date 2021-07-27
 *  @description
 **/
@Dao
abstract class BaseJJSDao: JJSDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(beans: Array<Expert>): List<Long>

    @Update
    abstract fun update(beans: Array<Expert>): Int

    @Query("""
        select * from tb_api_expert where exportId = :id
    """)
    abstract fun findExpertById(id: Int): Expert

    @Query("""
        select * from tb_api_expert
    """)
    abstract fun findAllExpert(): Flow<List<Expert>>

    @Query("""
        select exportId from tb_api_expert where exportId = :id
    """)
    abstract fun checkExpertExistsById(id: Int): Int

    @Transaction
    open fun insertAll(beans: List<ViewPoint>){
        insert(beans.toTypedArray())
        val experts = beans.map { viewPoint -> Expert(viewPoint.expertId, viewPoint.jSaleCount, 0,0) }.toTypedArray()
        insert(experts)
    }
}