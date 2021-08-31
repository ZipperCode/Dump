package com.zipper.auto.api.api.jd.delay

import android.content.Context
import com.zipper.auto.api.api.jd.JdBaseApi
import com.zipper.auto.api.api.service.ServiceManager
import com.zipper.core.utils.L
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JdDelayApi : JdBaseApi() {

    companion object{
        const val TAG: String = "JdDelayApi"
    }

    override fun domain(): String = "https://api.m.jd.com"

    private val jdDelaySignService: JdDelaySignService = retrofit.create(JdDelaySignService::class.java)

    override suspend fun execute(context: Context) {
        TODO("Not yet implemented")
    }

    fun jdBeanHome() {
        val response = jdDelaySignService.jdBeanHome().execute()
        if(response.isSuccessful){
            val jdBeanHome = response.body()
            jdBeanHome?.run {
                when{
                    code == "3" -> {
                        L.d(TAG, "京东商城-京豆Cookie失效")
                    }
                    data.status == "1" -> {
                        L.d(TAG, "京东商城-京豆签到成功")
                    }
                    data.status == "2" -> {
                        L.d(TAG, "京东商城-今天已签到，获得奖励")
                    }
                    else ->{
                        L.d(TAG, "京东商城-京豆: 失败, 原因: 未知 ⚠️")
                    }
                }
            }
        }
    }

    fun jdStore(){
        val response = jdDelaySignService.jdStore().execute()
        if(response.isSuccessful){
            val body = response.body()
            if(body?.data?.success == true || body?.data?.bizCode == 811){
                L.d(TAG, "京东商城-超市签到成功")
                return
            }
        }
        L.d(TAG, "京东商城-超市签到失败 ==> response = $response")
    }


    fun jdTurn(){
        val response = jdDelaySignService.jdTurn().execute()
        if(response.isSuccessful){
            val body = response.body()
            if(body?.data?.lotteryCode == "1"){
                jdTurnSign(body.data.lotteryCode)
                return
            }
        }
    }

    private fun jdTurnSign(code: String){
        val response = jdDelaySignService.jdTurnSign(mapOf("body" to "{\"actId\":\"jgpqtzjhvaoym\",\"appSource\":\"jdhome\",\"lotteryCode\":\"${code}\"}")).execute()
        if(response.isSuccessful){
            val body = response.body()
            when {
                body?.code == "3" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: Cookie失效‼️")
                }
                body?.errorCode == "T216" -> {
                    L.d(TAG, "抽奖已经结束")
                }
                body?.errorCode == "T215" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 已转过 ⚠️")
                }
                body?.errorCode == "T210" -> {
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 无支付密码 ⚠️")
                }
                body?.data?.chances != "0" -> {
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(2000)
                        jdTurnSign(code)
                    }
                }
                else ->{
                    L.d(TAG, "京东商城-转盘: 失败, 原因: 未知 ⚠️")
                }
            }
        }
    }

    fun jdSubsidy(){
        val response = jdDelaySignService.jdSubsidy().execute()
        if(response.isSuccessful){
            val body = response.body()
            if(body?.resultCode == 0 && body.resultData?.code == 0){
                L.d(TAG, "京东商城-金贴签到成功 ")
            }else if(body?.resultCode == 0 && body.resultData?.data?.thisAmount != 0){
                L.d(TAG, "京东商城-已经签到")
            }else{
                L.d(TAG, "京东商城-签到失败")
            }
        }
    }
}