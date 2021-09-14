package com.zipper.core.utils

import java.io.*

object FileUtil {

    fun readString(inputStream: InputStream): String{
        return BufferedReader(InputStreamReader(inputStream)).use {
            val stringBuilder = StringBuilder(inputStream.available())
            var line: String? = null
            do{
                line = it.readLine()
                stringBuilder.append(line).append("\n")
            }while (line != null)
            stringBuilder.toString()
        }
    }

    fun copyFile(inputStream: InputStream, outputStream: OutputStream){
        inputStream.use { input->
            outputStream.use { output->
                var len: Int
                val buff = ByteArray(1024 * 1024)
                len = input.read(buff)
                while (len != -1){
                    output.write(buff, 0,len)
                    len = input.read(buff)
                }
                output.flush()
            }
        }
    }
}