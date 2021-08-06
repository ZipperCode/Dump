package com.zipper.auto.api.script

import android.content.Context
import com.eclipsesource.v8.JavaVoidCallback
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Function
import com.eclipsesource.v8.V8Object
import com.eclipsesource.v8.utils.MemoryManager
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
        val stream = context.assets.open("jd/jd_bean_change.js") ?: return

        val stringBuilder = FileUtil.readString(stream)
//        val file = File(context.filesDir,"jd/ZooFaker_Necklace.js")
//        if(!file.exists()){
//            file.parentFile?.mkdirs()
//            file.createNewFile()
//            val stream1 = context.assets.open("jd/ZooFaker_Necklace.js")
//            val fileOutStream = FileOutputStream(file)
//            try {
//                val bis = BufferedInputStream(stream1)
//                val buffer = ByteArray(1024)
//                var len = 0
//                do{
//                    len = bis.read(buffer)
//                    fileOutStream.write(buffer, 0, len)
//                    fileOutStream.flush()
//                }while (len > 0)
//            }catch (e: Exception){
//                e.printStackTrace()
//            }finally {
//                stream1.close()
//            }
//        }

        v8Executor.execute {
            val v8Runtime = V8.createV8Runtime()
            val memoryManager = MemoryManager(v8Runtime)
            val j2v8Obj = V8Object(v8Runtime)
            v8Runtime.add("\$j2v8", j2v8Obj)
            registerSetTimeout(v8Runtime)
            registerConsole(v8Runtime)
            L.d("执行读取的脚本")
            try {
                v8Runtime.executeScript(stringBuilder)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            L.d("脚本执行完毕")
            memoryManager.release()
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

    private fun registerGet(v8Runtime: V8) {
        val callback = JavaVoidCallback { _, v8Array ->
            val v8Function = v8Array.getObject(0) as V8Function
            val time = v8Array.getInteger(1).toLong()
            v8Executor.schedule({
                v8Function.call(v8Runtime, null)
            }, time, TimeUnit.MILLISECONDS)
        }
        v8Runtime.registerJavaMethod(callback, "nativeGet")
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
        v8Console.release()
    }

    fun registerVoidCall(v8Runtime: V8, name: String, voidCallback: JavaVoidCallback) {
        v8Runtime.registerJavaMethod(voidCallback, name)
    }


    class Console {
        fun log(message: String) {
            L.d("log message = $message")
        }

        fun error(message: String) {
            L.e("error Message = $message")
        }
    }
}
