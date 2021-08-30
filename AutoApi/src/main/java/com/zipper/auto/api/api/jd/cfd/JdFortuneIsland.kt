package com.zipper.auto.api.api.jd.cfd

import android.R.attr.data
import android.content.Context
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.zipper.auto.api.api.jd.JdBaseApi
import com.zipper.core.utils.L
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*


class JdFortuneIsland : JdBaseApi() {
    override fun domain(): String = "m.jingxi.com"

    private val appId: String = "10028"

    private var fingerprint: String = ""

    private var enCryptMethodJD:String = ""

    private var username:String = ""

    private var isLogin: Boolean = false

    private var nickname: String = ""

    override suspend fun execute(context: Context) {
        requestAlgo()
        totalBean()
        if(!isLogin){
            L.d("京东账号 $username 未登录, 请重新获取token")
            return
        }
    }

    suspend fun requestAlgo() {
        fingerprint = generateFp()
        val url = "https://cactus.jd.com/request_algo?g_ty=ajax"
        val headers = mapOf(
            "Authority" to "cactus.jd.com",
            "Pragma" to "no-cache",
            "Cache-Control" to "no-cache",
            "Accept" to "application/json",
            "User-Agent" to "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
            "Content-Type" to "application/json",
            "Origin" to "https://st.jingxi.com",
            "Sec-Fetch-Site" to "cross-site",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Dest" to "empty",
            "Referer" to "https://st.jingxi.com/",
            "Accept-Language" to "zh-CN,zh;q=0.9,zh-TW;q=0.8,en;q=0.7"
        )
        val body = mapOf(
            "version" to "1.0",
            "fp" to fingerprint,
            "appId" to appId,
            "timestamp" to Date().time,
            "platform" to "web",
            "expandParams" to ""
        )

        try {
            val json = post(
                url,
                RequestBody.create(
                    "application/text".toMediaTypeOrNull(), gson.toJson(body)
                ), headers
            )
            println(json)
            val resultMap = convertMap(json)
            val status = resultMap["status"]!!.toString().toInt()
            if (status == 200) {
                val result = ((resultMap["data"] as Map<*, *>)["result"] as Map<*, *>)
                val token = result["tk"]
                val enCryptMethodJDString = result["algo"] as? String
                if(enCryptMethodJDString != null){
                    enCryptMethodJD = enCryptMethodJDString
                }
                L.d("获取签名参数成功！")
                L.d("fp: $fingerprint")
                L.d("token: $token")
                L.d("enCryptMethodJD: $enCryptMethodJDString")

            }else{
                L.d("fp: ${fingerprint}")
                L.d("request_algo 签名参数API请求失败:")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun totalBean(){
        val url = "https://me-api.jd.com/user_new/info/GetJDUserInfoUnion"
        val headers = mapOf(
            "Host" to "me-api.jd.com",
        "Accept" to "*/*",
        "Connection" to "keep-alive",
        "User-Agent" to userAgent,
        "Accept-Language" to "zh-cn",
        "Referer" to "https://home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&",
        "Accept-Encoding" to "gzip, deflate, br"
        )
        try {
            val json = get(
                url,
                headers
            )
            println(json)
            val resultMap = convertMap(json)
            val retcode = resultMap["retcode"]!!.toString()
            val data = resultMap["data"] as? Map<*, *>
            if(retcode == "1001"){
                isLogin = false
            }else if(retcode == "0" && data != null ){
                val baseInfo = data["userInfo"] as Map<*, *>
                nickname  = baseInfo["nickname"] as String
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    suspend fun getJxToken(){

    }


    /**
     * 模拟生成 fingerprint
     */
    fun generateFp(): String {
        val script = """
            function generateFp() {
                let e = "0123456789";
                let a = 13;
                let i = '';
                for (; a--;)
                    i += e[Math.random() * e.length | 0];
                return (i + Date.now()).slice(0, 16);
            }
        """.trimIndent()
        val v8Runtime = V8.createV8Runtime()
        v8Runtime.executeScript(script)
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            result = v8Runtime.executeStringFunction("generateFp", param)
            L.d("generateFp() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
            v8Runtime.release()
        }
        return result
    }


}