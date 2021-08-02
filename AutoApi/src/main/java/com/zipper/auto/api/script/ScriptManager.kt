package com.zipper.auto.api.script

import android.content.Context
import com.eclipsesource.v8.*
import com.eclipsesource.v8.utils.MemoryManager
import com.eclipsesource.v8.utils.V8ObjectUtils
import com.zipper.core.utils.L
import java.io.*

object ScriptManager {

    private val cacheScript: MutableMap<String, V8> = mutableMapOf()

    fun init(){

    }

    fun getScript(context: Context, scriptName: String) {
        val stream = context.assets.open("jd/jd_necklance.js") ?: return

        val stringBuilder = StringBuilder(stream.available())
        try {
            val bufferedReader = BufferedReader(InputStreamReader(stream))
            var line: String? = null
            do{
                line = bufferedReader.readLine()
                stringBuilder.append(line).append("\n")
            }while (line != null)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            stream.close()
        }
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

        val nodejs = NodeJS.createNodeJS()

        val v8Runtime = V8.createV8Runtime(scriptName)
        val memoryManager = MemoryManager(v8Runtime)

        val callback = JavaVoidCallback { p0, p1 ->
            L.d("收到js回调结果 $p0 参数为: ${p1[0]}")
        }
        val console = Console()
        val consoleObj = V8Object(v8Runtime)
        v8Runtime.add("console", consoleObj)
        v8Runtime.registerJavaMethod(console, "log", "log", arrayOf(String::class.java))
        v8Runtime.registerJavaMethod(callback, "Java")

        v8Runtime.executeScript("""
            async function test(){
                Java("test成功回调1")
                console.log("hello world")
                await wait(2000)
                Java("test成功回调2")
            }
            
            function wait(t) {
                return new Promise(e => setTimeout(e, t))
            }
            
            function test2(){
                Java("test2成功回调")
                test().then(res => {
                    console.log("test then result")
                })
            }
        """.trimIndent())

        val result = v8Runtime.executeJSFunction("test", V8Array(v8Runtime))
        val result2 = v8Runtime.executeJSFunction("test2", V8Array(v8Runtime))
        L.d("result is func ${result is V8Function}")
        L.d("result is v8Object ${result is V8Object}")

        L.d("result2 is func ${result is V8Function}")
        L.d("result2 is v8Object ${result is V8Object}")
        L.d("result2 = $result2")

//        val param = V8Array(v8Runtime).push(1).push(2)
//        v8Runtime.executeScript(stringBuilder.toString())
//        val utils = v8Runtime.getObject("utils")
//        L.d("utils = $utils")
//        if(utils != null){
//            L.d("utils = ${V8ObjectUtils.getValue(utils)}")
//        }
//        val result = v8Runtime.executeIntegerFunction("safeAdd", param)
//        L.d("result_aaa = ${v8Runtime.executeStringFunction("aaa", V8Array(v8Runtime))}")
        L.d("result = $result")
        memoryManager.release()
    }

    class Console{
        fun log(message: String){
            L.d("message = $message")
        }
    }
}
