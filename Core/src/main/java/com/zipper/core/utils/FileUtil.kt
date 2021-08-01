package com.zipper.core.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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
}