package com.zipper.plugin.dump.room

import androidx.room.*
import com.zipper.plugin.dump.bean.ViewInfo

@Dao
interface ViewInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg viewInfo: ViewInfo)

    @Update
    fun update(viewInfo: ViewInfo)

    @Delete
    fun delete(viewInfo: ViewInfo)

    @Query("SELECT * FROM tb_dump_view_info")
    fun getAll(): List<ViewInfo>

    @Query("SELECT * FROM tb_dump_view_info WHERE id = :id")
    fun queryById(id: Int): ViewInfo

    @Query("SELECT * FROM tb_dump_view_info WHERE package_name = :packageName")
    fun queryByPackageName(packageName: String): List<ViewInfo>

    @Query("SELECT * FROM tb_dump_view_info WHERE view_id = :viewId and package_name = :packageName")
    fun queryByViewIdAndPackageName(viewId: String, packageName: String): List<ViewInfo>
}