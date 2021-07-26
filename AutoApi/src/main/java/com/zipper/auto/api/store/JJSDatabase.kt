package com.zipper.auto.api.store

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zipper.auto.api.bean.ViewPoint

@Database(entities = [ViewPoint::class], version = 2)
abstract class JJSDatabase : RoomDatabase() {

    abstract fun getJJSDao(): JJSDao

    companion object{

        private var db: JJSDatabase? = null

        fun openDatabase(context: Context): JJSDatabase{
            if (db == null) {
                synchronized(this) {
                    if (db == null) {
                        db = Room    // 创建数据库创建器
                            .databaseBuilder(context, JJSDatabase::class.java, "db_jjs")
                            .build()
                    }
                }
            }
            return db!!
        }

    }
}