package com.zipper.dump.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DBHelper {

    private var db: ViewInfoDatabase? = null

    fun openViewInfoDatabase(context: Context): ViewInfoDatabase {
        if (db == null) {
            synchronized(this) {
                if (db == null) {
                    db = Room    // 创建数据库创建器
                        .databaseBuilder(context, ViewInfoDatabase::class.java, "db_dump")
                        .build()
                }
            }
        }
        return db!!
    }

    fun getViewInfoDao(): ViewInfoDao {
        return db!!.getViewInfoDao()
    }
}