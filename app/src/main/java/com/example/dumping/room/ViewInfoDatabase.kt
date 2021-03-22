package com.example.dumping.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dumping.bean.ViewInfo
import com.example.dumping.convert.RectConverters

@Database(entities = [ViewInfo::class] ,version = 1)
@TypeConverters(RectConverters::class)
abstract class ViewInfoDatabase: RoomDatabase() {

    abstract fun getViewInfoDao(): ViewInfoDao
}