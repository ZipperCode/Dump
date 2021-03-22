package com.example.dumping.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dumping.bean.ViewInfo

@Dao
interface ViewInfoDao {

    @Insert
    fun insert(viewInfo: ViewInfo)

    @Update
    fun update(viewInfo: ViewInfo)

    @Delete
    fun delete(viewInfo: ViewInfo)

    @Query("SELECT * FROM tb_dump_view_info")
    fun getAll():List<ViewInfo>

    @Query("SELECT * FROM tb_dump_view_info WHERE id = :id")
    fun queryById(id: Int): ViewInfo

    @Query("SELECT * FROM tb_dump_view_info WHERE package_name = :packageName")
    fun queryByPackageName(packageName: String): List<ViewInfo>
}