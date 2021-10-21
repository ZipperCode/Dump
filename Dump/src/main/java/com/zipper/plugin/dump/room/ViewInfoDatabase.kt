package com.zipper.plugin.dump.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zipper.plugin.dump.bean.ViewInfo
import com.zipper.plugin.dump.convert.RectConverters

@Database(entities = [ViewInfo::class], version = 2, exportSchema = false)
@TypeConverters(RectConverters::class)
abstract class ViewInfoDatabase : RoomDatabase() {

    abstract fun getViewInfoDao(): ViewInfoDao
}