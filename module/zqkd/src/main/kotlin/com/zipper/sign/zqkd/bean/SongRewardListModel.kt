package com.zipper.sign.zqkd.bean

import com.google.gson.annotations.SerializedName


data class SongRewardListModel(
    val more_score: Int = 0,
    val more_sec: Int = 0,
    val list: List<SongRewardModel> = listOf()
) {

    data class SongRewardModel(
        @SerializedName("level")
        val level: Int = 0,
        @SerializedName("score")
        val score: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("sec")
        val time: Int = 0,
        @SerializedName("total")
        val totalTime: Int = 0,
    )
}
