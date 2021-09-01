package com.zipper.auto.api.script

import android.content.Context
import com.eclipsesource.v8.*
import com.eclipsesource.v8.utils.MemoryManager
import com.eclipsesource.v8.utils.V8ObjectUtils
import com.google.gson.Gson
import com.zipper.core.utils.FileUtil
import com.zipper.core.utils.L
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object ScriptManager {

    private val cacheScript: MutableMap<String, V8> = mutableMapOf()

    private val v8Executor = Executors.newSingleThreadScheduledExecutor()

    fun init() {

    }

    val testScript = """
            async function test(){
                console.log("执行Test方法，并等待2s");
                await wait(2000)
                console.log("执行Test方法，等待2s之后");
                return new Promise(resolve => {
                    console.log("执行Test方法中的Promise resolve");
                    resolve("[我是resolve响应结果]");
                })
            }
            
            function wait(t) {
                return new Promise(e => setTimeout(e, t))
            }
            
            function test2(){
                console.log("执行test2")
                test().then(res => {
                    console.log("收到异步消息 result = ",res);
                }).catch(error => {
                    console.log("收到异步error", error)
                });
            }
            var id = setTimeout(test2,2000);
            
            console.log("id = " + id)
        """.trimIndent()

    fun getScript(context: Context, scriptName: String) {
//        val stream = context.assets.open("jd/jd_cfd.js") ?: return
//        val stream1 = context.assets.open("jd/env.js") ?: return
//
//        val script = FileUtil.readString(stream)
//        val env = FileUtil.readString(stream1)

        val stream = context.assets.open("lib/sockjs.min.js")
        val sock  = FileUtil.readString(stream)
        v8Executor.execute {
            val v8Runtime = V8.createV8Runtime()
            registerConsole(v8Runtime)
            registerSetTimeout(v8Runtime)
            L.d("脚本sock执行")
            v8Runtime.executeScript(sock)
            L.d("脚本sock执行完毕")
            val script = """
                 console.log("0000000000000000000000000000000000");
                 var sock = new SockJS('https://mydomain.com/my_prefix');
                 console.log("sock = " + sock);
                 sock.onopen = function () {
                     console.log('open');
                     sock.send('test');
                 };

                 sock.onmessage = function (e) {
                     console.log('message', e.data);
                     sock.close();
                 };

                 sock.onclose = function () {
                     console.log('close');
                 };
            """.trimIndent()
            v8Runtime.executeScript(script)
//            v8Runtime.close()
            L.d("脚本执行完毕")
        }
//        val result = v8Runtime.executeJSFunction("test", V8Array(v8Runtime))
//        val result2 = v8Runtime.executeJSFunction("test2", V8Array(v8Runtime))

//        val param = V8Array(v8Runtime).push(1).push(2)
//        v8Runtime.executeScript(stringBuilder.toString())
//        val utils = v8Runtime.getObject("utils")
//        L.d("utils = $utils")
//        if(utils != null){
//            L.d("utils = ${V8ObjectUtils.getValue(utils)}")
//        }
//        val result = v8Runtime.executeIntegerFunction("safeAdd", param)
//        L.d("result_aaa = ${v8Runtime.executeStringFunction("aaa", V8Array(v8Runtime))}")
//        L.d("result = $result")
//        memoryManager.release()
    }

    fun runScript(){
        val v8Runtime = V8.createV8Runtime()
//            val memoryManager = MemoryManager(v8Runtime)
        val j2v8Obj = V8Object(v8Runtime)
        v8Runtime.add("\$j2v8", j2v8Obj)
        registerSetTimeout(v8Runtime)
        registerConsole(v8Runtime)
        registerGet(v8Runtime)
        registerPost(v8Runtime)
        registerGetCookieFun(v8Runtime, "pt_key=AAJhDK27ADAFKRijGch2mX7SLk8nVDIfE8nOD00iT7eyFvmFgFmRHZLnU37Qc9Yneml4qsdsP8E;pt_pin=jd_59739e7a7a296;")
        L.d("执行读取的脚本")
        try {
            v8Runtime.executeScript(testScript)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        L.d("脚本执行完毕")
    }

    private fun registerGet(v8Runtime: V8) {
        val callback = JavaVoidCallback { _, v8Array ->
            val requestData = v8Array.getString(0)
            val resolveFunction = (v8Array.getObject(1) as V8Function).twin()
            val rejectFunction = v8Array.getObject(2) as V8Function
            try {
                val jsRequestBean: JsRequestBean = Gson().fromJson(requestData, JsRequestBean::class.java)
                L.d("JavaScript -> Java Thread = ${Thread.currentThread()}")
                HttpHelper.get(jsRequestBean){
                    onRequestResult(v8Runtime, resolveFunction,rejectFunction,it)
                }
            }catch (e: Exception){
                e.printStackTrace()
                onRequestResult(v8Runtime, resolveFunction,rejectFunction,
                    HttpResponseBean(-1,e.message ?: "错误")
                )
            }
        }
        v8Runtime.registerJavaMethod(callback, "nativeGet")
    }

    private fun registerPost(v8Runtime: V8) {
        val callback = JavaVoidCallback { _, v8Array ->
            val requestData = v8Array.getString(0)
            val resolveFunction = (v8Array.getObject(1) as V8Function).twin()
            val rejectFunction = v8Array.getObject(2) as V8Function
            try {
                val jsRequestBean: JsRequestBean = Gson().fromJson(requestData, JsRequestBean::class.java)
                HttpHelper.post(jsRequestBean){
                    onRequestResult(v8Runtime, resolveFunction,rejectFunction,it)
                }
            }catch (e: Exception){
                e.printStackTrace()
                onRequestResult(v8Runtime, resolveFunction,rejectFunction,
                    HttpResponseBean(-1,e.message ?: "错误")
                )
            }
        }
        v8Runtime.registerJavaMethod(callback, "nativePost")
    }

    private fun onRequestResult(v8Runtime: V8,resolveFunction: V8Function,rejectFunction: V8Function,  responseBean: HttpResponseBean){
        v8Executor.execute{
            try {
                if(responseBean.status == 200){
                    val param = V8Array(v8Runtime)
                    resolveFunction.call(v8Runtime, null)
                    param.close()
                }else{
                    val param = V8Array(v8Runtime)
                    rejectFunction.call(v8Runtime, param.push(responseBean.json))
                    param.close()
                }
            }finally {
                resolveFunction.close()
                rejectFunction.close()
            }
        }
    }
    /**
     * 注册setTimeout函数
     */
    private fun registerSetTimeout(v8Runtime: V8) {
        val callback = JavaVoidCallback { _, v8Array ->
            val v8Function = v8Array.getObject(0) as V8Function
            val time = v8Array.getInteger(1).toLong()
            v8Executor.schedule({ v8Function.call(v8Runtime, null) }, time, TimeUnit.MILLISECONDS)
        }
        v8Runtime.registerJavaMethod(callback, "setTimeout")
    }

    private fun registerConsole(v8Runtime: V8) {
        val console = Console()
        val v8Console = V8Object(v8Runtime)
        v8Runtime.add("console", v8Console)
        v8Console.registerJavaMethod(
            console,
            "log",
            "log",
            arrayOf<Class<*>>(String::class.java)
        )
        v8Console.registerJavaMethod(
            console,
            "error",
            "error",
            arrayOf<Class<*>>(String::class.java)
        )
        v8Console.close()
    }

    private fun replaceEnvCode(script: String, newEnv: String): String{
        val index = script.indexOf("// prettier-ignore")
        L.d("查找到index = $index")
        if(index > 0){
            return script.substring(0, index) + "\n" + newEnv
        }
        return script
    }

    private fun registerGetCookieFun(v8Runtime: V8, jdCookie: String){
        v8Runtime.registerJavaMethod(JavaCallback { _, v8Array ->
            jdCookie
        }, "nativeGetCookie")
    }

    fun registerVoidCall(v8Runtime: V8, name: String, voidCallback: JavaVoidCallback) {
        v8Runtime.registerJavaMethod(voidCallback, name)
    }

    fun v8Object2Json(v8Runtime: V8, v8Object: V8Object): String {
        try {
            val json: V8Object = v8Runtime.getObject("JSON")
            val parameters = V8Array(v8Runtime).push(v8Object)
            val result = json.executeStringFunction("stringify", parameters)
            parameters.release()
            json.release()
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            v8Object.release()
        }
        return "{}"
    }


    class Console {
        fun log(message: String) {
            L.d("【JS】 $message")
        }

        fun error(message: String) {
            L.e("【JS】 $message")
        }
    }
}
