package com.zipper.auto.api.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_api_expert")
data class Expert(
    @PrimaryKey()
    val exportId:Int,
    val scaleNum: Int,
    val successNum: Int,
    val failureNum: Int
)
