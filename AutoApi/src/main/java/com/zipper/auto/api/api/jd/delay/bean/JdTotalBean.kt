package com.zipper.auto.api.api.jd.delay.bean

data class JdTotalBean(
    val `data`: Data?,
    val msg: String,
    val retcode: String,
    val timestamp: Long
){

    data class Data(
        val JdVvipCocoonInfo: JdVvipCocoonInfo,
        val JdVvipInfo: JdVvipInfo,
        val assetInfo: AssetInfo,
        val favInfo: FavInfo,
        val growHelperCoupon: GrowHelperCoupon,
        val orderInfo: OrderInfo,
        val plusPromotion: PlusPromotion,
        val userInfo: UserInfo,
        val userLifeCycle: UserLifeCycle
    )

    data class JdVvipCocoonInfo(
        val JdVvipCocoonStatus: String
    )

    data class JdVvipInfo(
        val jdVvipStatus: String
    )


    data class AssetInfo(
        val accountBalance: String,
        val baitiaoInfo: BaitiaoInfo,
        val beanNum: String,
        val couponNum: String,
        val couponRed: String,
        val redBalance: String
    )

    data class BaitiaoInfo(
        val availableLimit: String,
        val baiTiaoStatus: String,
        val bill: String,
        val billOverStatus: String,
        val outstanding7Amount: String,
        val overDueAmount: String,
        val overDueCount: String,
        val unpaidForAll: String,
        val unpaidForMonth: String
    )

    data class FavInfo(
        val favDpNum: String,
        val favGoodsNum: String,
        val favShopNum: String,
        val footNum: String,
        val isGoodsRed: String,
        val isShopRed: String
    )

    data class GrowHelperCoupon(
        val addDays: Int,
        val batchId: Int,
        val couponKind: Int,
        val couponModel: Int,
        val couponStyle: Int,
        val couponType: Int,
        val discount: Double,
        val limitType: Int,
        val msgType: Int,
        val quota: Double,
        val roleId: Int,
        val state: Int,
        val status: Int
    )


    data class BaseInfo(
        val accountType: String,
        val baseInfoStatus: String,
        val curPin: String,
        val definePin: String,
        val headImageUrl: String,
        val levelName: String,
        val nickname: String,
        val pinlist: String,
        val userLevel: String
    )

    data class OrderInfo(
        val commentCount: String,
        val logistics: List<Any>,
        val orderCountStatus: String,
        val receiveCount: String,
        val waitPayCount: String
    )

    data class UserInfo(
        val baseInfo: BaseInfo,
        val isHideNavi: String,
        val isHomeWhite: String,
        val isJTH: String,
        val isKaiPu: String,
        val isPlusVip: String,
        val isQQFans: String,
        val isRealNameAuth: String,
        val isWxFans: String,
        val jvalue: String,
        val orderFlag: String,
        val plusInfo: PlusInfo,
        val xbScore: String
    )

    class PlusInfo

    data class PlusPromotion(
        val status: Int
    )

    data class UserLifeCycle(
        val identityId: String,
        val lifeCycleStatus: String,
        val trackId: String
    )
}