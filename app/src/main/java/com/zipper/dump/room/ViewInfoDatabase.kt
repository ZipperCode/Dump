package com.zipper.dump.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.convert.RectConverters

@Database(entities = [ViewInfo::class], version = 2)
@TypeConverters(RectConverters::class)
abstract class ViewInfoDatabase : RoomDatabase() {

    abstract fun getViewInfoDao(): ViewInfoDao
}