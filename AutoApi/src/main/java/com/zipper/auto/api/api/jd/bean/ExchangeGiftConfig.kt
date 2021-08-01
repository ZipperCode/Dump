package com.zipper.auto.api.api.jd.bean


import com.google.gson.annotations.SerializedName

data class ExchangeGiftConfig(
    @SerializedName("alreadyExchanged")
    val alreadyExchanged: Int = 0,
    @SerializedName("batchId")
    val batchId: String = "",
    @SerializedName("couponAmount")
    val couponAmount: String = "",
    @SerializedName("couponGoodsImg")
    val couponGoodsImg: String = "",
    @SerializedName("couponGoodsName")
    val couponGoodsName: String = "",
    @SerializedName("couponGoodsPrice")
    val couponGoodsPrice: String = "",
    @SerializedName("couponGoodsSku")
    val couponGoodsSku: String = "",
    @SerializedName("couponKey")
    val couponKey: String = "",
    @SerializedName("couponKind")
    val couponKind: Int = 0,
    @SerializedName("couponLimit")
    val couponLimit: String = "",
    @SerializedName("couponTitle")
    val couponTitle: String = "",
    @SerializedName("couponType")
    val couponType: Int = 0,
    @SerializedName("displayOrder")
    val displayOrder: Int = 0,
    @SerializedName("endTime")
    val endTime: String = "",
    @SerializedName("expireDay")
    val expireDay: Int = 0,
    @SerializedName("expireType")
    val expireType: Int = 0,
    @SerializedName("freeTag")
    val freeTag: Int = 0,
    @SerializedName("giftType")
    val giftType: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("isFuse")
    val isFuse: Boolean = false,
    @SerializedName("jumpLink")
    val jumpLink: String = "",
    @SerializedName("redpacketTitle")
    val redpacketTitle: String = "",
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("stockByHourInfo")
    val stockByHourInfo: String = "",
    @SerializedName("stockStatus")
    val stockStatus: Int = 0,
    @SerializedName("stockType")
    val stockType: Int = 0
)