package com.zipper.auto.api.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
@Entity(tableName = "tb_api_info")
class ViewPoint {
    @PrimaryKey()
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("expert_id")
    var expertId: Int = 0

    @SerializedName("type")
    var type: Int = 0

    @SerializedName("createTime")
    var createTime: String? = null

    @SerializedName("title")
    @Ignore
    var title: String? = null

    @SerializedName("description")
    @Ignore
    var description: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("price")
    var price: Float = 0.0f

    @SerializedName("notPointRefund")
    @Ignore
    var notPointRefund: String? = ""

    @SerializedName("focus")
    @ColumnInfo(name = "focus", defaultValue = "0")
    var focus: String? = "0"

    @SerializedName("jSaleCount")
    var jSaleCount: Int = 0

    @SerializedName("saleCount")
    var saleCount: Int = 0

    @SerializedName("returnPrize")
    @Ignore
    var returnPrize: String? = "0.0"

    @SerializedName("payBack")
    @Ignore
    var payBack: String? = "0"

    @SerializedName("status")
    var status: Int = 0

    @SerializedName("isSettle")
    @ColumnInfo(name = "isSettle")
    var settle: Int? = 0

    @SerializedName("audit")
    @ColumnInfo(name = "audit", defaultValue = "0")
    var audit: String? = ""

    @SerializedName("matchTime")
    var matchTime: Long = 0

    @SerializedName("cid")
    var cid: Int = 0
}