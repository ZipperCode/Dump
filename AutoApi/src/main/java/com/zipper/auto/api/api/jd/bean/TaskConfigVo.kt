package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class TaskConfigVo(
    @SerializedName("advertGroupId")
    val advertGroupId: String = "",
    @SerializedName("displayOrder")
    val displayOrder: Int = 0,
    @SerializedName("endTime")
    val endTime: String = "",
    @SerializedName("groupItemCount")
    val groupItemCount: Int = 0,
    @SerializedName("iconLink")
    val iconLink: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("jumpLink")
    val jumpLink: String = "",
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("taskName")
    val taskName: String = "",
    @SerializedName("taskStage")
    val taskStage: Int = 0,
    @SerializedName("taskSubtitle")
    val taskSubtitle: String = "",
    @SerializedName("taskType")
    val taskType: Int = 0
)