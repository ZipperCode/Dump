package com.zipper.auto.api.script

import android.content.Context
import com.eclipsesource.v8.NodeJS
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.utils.MemoryManager
import com.eclipsesource.v8.utils.V8ObjectUtils
import com.zipper.core.utils.L
import java.io.*

object ScriptManager {

    private val cacheScript: MutableMap<String, V8> = mutableMapOf()

    fun init(){

    }

    fun getScript(context: Context, scriptName: String) {
        val stream = context.assets.open("jd/ZooFaker_Necklace.js") ?: return

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
        val file = File(context.filesDir,"jd/ZooFaker_Necklace.js")
        if(!file.exists()){
            file.parentFile?.mkdirs()
            file.createNewFile()
            val stream1 = context.assets.open("jd/ZooFaker_Necklace.js")
            val fileOutStream = FileOutputStream(file)
            try {
                val bis = BufferedInputStream(stream1)
                val buffer = ByteArray(1024)
                var len = 0
                do{
                    len = bis.read(buffer)
                    fileOutStream.write(buffer, 0, len)
                    fileOutStream.flush()
                }while (len > 0)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                stream1.close()
            }
        }

        val v8Runtime = V8.createV8Runtime(scriptName)

        val memoryManager = MemoryManager(v8Runtime)

        val param = V8Array(v8Runtime).push(1).push(2)
        v8Runtime.executeScript(stringBuilder.toString())
        val utils = v8Runtime.getObject("utils")
        L.d("utils = $utils")
        if(utils != null){
            L.d("utils = ${V8ObjectUtils.getValue(utils)}")
        }
        val result = v8Runtime.executeIntegerFunction("safeAdd", param)
        L.d("result_aaa = ${v8Runtime.executeStringFunction("aaa", V8Array(v8Runtime))}")
        L.d("result = $result")
        memoryManager.release()
    }
}