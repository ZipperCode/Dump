package com.zipper.auto.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *  @author zipper
 *  @date 2021-07-23
 *  @description
 **/
class JJSApi: BaseApi() {

    companion object{
        const val BaseUrl = "http://jj.jinqiushao.com:8686/"
        // 同步
        const val GET_SYNC = BaseUrl + "api/sync"
        // 数据
        const val GET_INDEX = BaseUrl + ""
        // 修改观点金额
        const val GET_MOD_MONEY = BaseUrl + "api/modMoney?id=%s&money=%"
        // 修改标题
        const val GET_MOD_TITLE = BaseUrl + "api/modTitle?id=%s&title=%s"
        // 同步指定数据
        const val GET_SYNC_SINGLE = BaseUrl + "api/syncSingle?id=%s"
        // 修改推荐方案
        const val GET_RECOMMEND = BaseUrl + "api/reCommend?plan_id=%s"
        // 手动判断任e球方案
        const val GET_DETERMINE = BaseUrl + "api/modDetermine?plan_id=%s&dd_id=%s"
        // 手动判断进球文章
        const val GET_JJ_DETERMINE = BaseUrl + "api/modjjDetermine?plan_id=%s&dd_id=%s"
        // 修改比赛状态
        const val GET_MATCH_STATUS = BaseUrl + "api/modMatchStatus?match_id=%s&status_id=%s"

        const val GET_A = BaseUrl + "api/intro"

        const val GET_CHECK = BaseUrl + "api/check"
    }

    suspend fun index(): List<ViewPoint>{
        val url = "${BaseUrl}api/index/"
        val result = get(url)
        return Gson().fromJson(result, object : TypeToken<List<ViewPoint>>(){}.type)
    }


}