package com.example.dumping.room

import android.content.Context
import androidx.room.Room

object DBHelper {

    private var db: ViewInfoDatabase? = null

    fun openViewInfoDatabase(context: Context): ViewInfoDatabase{
        if(db == null){
            synchronized(this){
                if(db == null){
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