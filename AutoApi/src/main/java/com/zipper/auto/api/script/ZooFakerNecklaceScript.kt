package com.zipper.auto.api.script

import android.content.Context
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import com.eclipsesource.v8.utils.MemoryManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.utils.FileUtil
import com.zipper.core.utils.L


class ZooFakerNecklaceScript {

    private lateinit var script: String

    private lateinit var v8Runtime: V8

    private lateinit var memoryManager: MemoryManager

    fun init(context: Context) {
        val stream = context.assets.open("jd/ZooFaker_Necklace.js")
        script = FileUtil.readString(stream)
        v8Runtime = V8.createV8Runtime(ZooFakerNecklaceScript::class.java.simpleName)
        memoryManager = MemoryManager(v8Runtime)
        v8Runtime.executeScript(script)
    }

    fun decipherJoyToken(joyyToken: String, appId: String): Map<String, Any> {
        var result = "{}"
        val param = V8Array(v8Runtime)
        try {
            param.push("${appId}${joyyToken}").push(appId)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            val v8Result = utilsObj.executeObjectFunction("decipherJoyToken",param)
            L.d("v8Result = $v8Result")
            result = v8Object2Json(v8Result)
            L.d("jsonResult = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return Gson().fromJson(result, object : TypeToken<Map<String, Any>>() {}.type)
    }


    fun getKey(id: String, randomWord: String, time: String): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            param.push(id).push(randomWord).push(time)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            result = utilsObj.executeStringFunction("getKey",param)
            L.d("getKey() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun sha1(text:String): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            param.push(text)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            result = utilsObj.executeStringFunction("sha1",param)
            L.d("sha1() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun getCrcCode(text: String): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            param.push(text)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            result = utilsObj.executeStringFunction("getCrcCode",param)
            L.d("getCrcCode() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun getTouchSession(): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            result = utilsObj.executeStringFunction("getTouchSession",param)
            L.d("getTouchSession() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun hexMD5(text:String): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            param.push(text)
            result = v8Runtime.executeStringFunction("hexMD5",param)
            L.d("hexMD5() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun xorEncrypt(text: String, key: String): String{
        var result = ""
        val param = V8Array(v8Runtime)
        try {
            param.push(text).push(key)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            result = utilsObj.executeStringFunction("xorEncrypt",param)
            L.d("xorEncrypt() ==> result = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return result
    }

    fun get_risk_result(joyytoken: String, joyytoken_count: Int, action: String, id: Int,userName: String, UUID: String ){
        var result = "{}"
        val param = V8Array(v8Runtime)
        try {
            param.push(joyytoken).push(joyytoken_count).push(action).push(id).push(userName).push(UUID)
            val utilsObj = v8Runtime.getObject("utils")
            L.d("utilsObj = $utilsObj")
            val v8Result = utilsObj.executeObjectFunction("get_risk_result",param)
            L.d("v8Result = $v8Result")
            result = v8Object2Json(v8Result)
            L.d("jsonResult = $result")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            param.release()
        }
        return Gson().fromJson(result, object : TypeToken<Map<String, Any>>() {}.type)
    }

    fun getRandomWord(len: Int): String {
        val builder = StringBuilder(len)
        val n = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for (r in 0 until len) {
            val i = Math.round(Math.random() * (n.length - 1)).toInt()
            builder.append(n.substring(i, i + 1))
        }
        return builder.toString()
    }

    fun v8Object2Json(v8Object: V8Object): String {
        val v8 = V8.createV8Runtime("tmp")
        try {
            val json: V8Object = v8.getObject("JSON")
            val parameters = V8Array(v8).push(v8Object)
            val result = json.executeStringFunction("stringify", parameters)
            parameters.release()
            json.release()
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            v8.release()
            v8Object.release()
        }
        return "{}"
    }
}
//    fun decipherJoyToken(joyyToken: String, appId: String): Map<String,Any>{
//        val script = """
//        function atobPolyfill(e){var t="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";if(e=String(e).replace(/[\t\n\f\r ]+/g,""),!/^(?:[A-Za-z\d+\/]{4})*?(?:[A-Za-z\d+\/]{2}(?:==)?|[A-Za-z\d+\/]{3}=?)?${'$'}/.test(e)){throw new TypeError("解密错误")}e+="==".slice(2-(3&e.length));for(var n,r,i,o="",a=0;a<e.length;){n=t.indexOf(e.charAt(a++))<<18|t.indexOf(e.charAt(a++))<<12|(r=t.indexOf(e.charAt(a++)))<<6|(i=t.indexOf(e.charAt(a++))),o+=64===r?String.fromCharCode(n>>16&255):64===i?String.fromCharCode(n>>16&255,n>>8&255):String.fromCharCode(n>>16&255,n>>8&255,255&n)}return o}function xorEncrypt(e,t){for(var n=t.length,r="",i=0;i<e.length;i++){r+=String.fromCharCode(e[i].charCodeAt()^t[i%n].charCodeAt())}return r}function decipherJoyToken(e,t){return JSON.stringify(function(e,t){var n={jjt:"a",expire:new Date().getTime(),outtime:3,time_correction:!1};var r="",i=e.indexOf(t)+t.length,o=e.length;if((r=(r=e.slice(i,o).split(".")).map(function(e){return atobPolyfill(e)}))[1]&&r[0]&&r[2]){var a=r[0].slice(2,7),s=r[0].slice(7,9),u=xorEncrypt(r[1]||"",a).split("~");n.outtime=u[3]-0,n.encrypt_id=u[2],n.jjt="t";var c=u[0]-0||0;c&&"number"==typeof c&&(n.time_correction=!0,n.expire=c);var l=c-new Date().getTime()||0;return n.q=l,n.cf_v=s,n}return n}(e,t))};
//        """.trimIndent()
//        val v8 = V8.createV8Runtime()
//        var result = "{}"
//        try {
//            v8.executeScript(script)
//            val param = V8Array(v8)
//            param.push("${appId}${joyyToken}").push(appId)
//            result = v8.executeStringFunction("decipherJoyToken", param)
//            param.release()
//        }catch (e: Exception){
//            e.printStackTrace()
//        }finally {
//            v8.release(true)
//        }
//        return Gson().fromJson(result,object :TypeToken<Map<String,Any>>(){}.type)
//    }
//
//    fun sha1(text: String): String{
//        val script = "function encodeUTF8(s){var i,r=[],c,x;for(i=0;i<s.length;i++){if((c=s.charCodeAt(i))<128){r.push(c)}else{if(c<2048){r.push(192+(c>>6&31),128+(c&63))}else{if((x=c^55296)>>10==0){c=(x<<10)+(s.charCodeAt(++i)^56320)+65536,r.push(240+(c>>18&7),128+(c>>12&63))}else{r.push(224+(c>>12&15))}r.push(128+(c>>6&63),128+(c&63))}}}return r}function sha1(s){var data=new Uint8Array(encodeUTF8(s));var i,j,t;var l=((data.length+8)>>>6<<4)+16,s=new Uint8Array(l<<2);s.set(new Uint8Array(data.buffer)),s=new Uint32Array(s.buffer);for(t=new DataView(s.buffer),i=0;i<l;i++){s[i]=t.getUint32(i<<2)}s[data.length>>2]|=128<<(24-(data.length&3)*8);s[l-1]=data.length<<3;var w=[],f=[function(){return m[1]&m[2]|~m[1]&m[3]},function(){return m[1]^m[2]^m[3]},function(){return m[1]&m[2]|m[1]&m[3]|m[2]&m[3]},function(){return m[1]^m[2]^m[3]}],rol=function(n,c){return n<<c|n>>>(32-c)},k=[1518500249,1859775393,-1894007588,-899497514],m=[1732584193,-271733879,null,null,-1009589776];m[2]=~m[0],m[3]=~m[1];for(var i=0;i<s.length;i+=16){var o=m.slice(0);for(j=0;j<80;j++){w[j]=j<16?s[i+j]:rol(w[j-3]^w[j-8]^w[j-14]^w[j-16],1),t=rol(m[0],5)+f[j/20|0]()+m[4]+w[j]+k[j/20|0]|0,m[1]=rol(m[1],30),m.pop(),m.unshift(t)}for(j=0;j<5;j++){m[j]=m[j]+o[j]|0}}t=new DataView(new Uint32Array(m).buffer);for(var i=0;i<5;i++){m[i]=t.getUint32(i<<2)}var hex=Array.prototype.map.call(new Uint8Array(new Uint32Array(m).buffer),function(e){return(e<16?\"0\":\"\")+e.toString(16)}).join(\"\");return hex.toString().toUpperCase()};"
//        val v8 = V8.createV8Runtime()
//        var result = ""
//        try {
//            v8.executeScript(script)
//            val param = V8Array(v8)
//            param.push("text")
//            result = v8.executeStringFunction("sha1", param)
//            param.release()
//        }catch (e: Exception){
//            e.printStackTrace()
//        }finally {
//            v8.release(true)
//        }
//        return result
//    }
//

