package com.zipper.sign.gdbh

import com.zipper.sign.core.millsTimeStr
import com.zipper.sign.gdbh.bean.BaseResp
import com.zipper.sign.gdbh.bean.CheckInBody
import com.zipper.sign.gdbh.bean.CoinInfo
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GdbhService {

    @GET("member/profile/coins/info")
    suspend fun info(): BaseResp<CoinInfo>

    @GET("coins/checkin")
    suspend fun checkIn(): BaseResp<Any>

    @GET("coins/award")
    suspend fun award(): BaseResp<Any>
}