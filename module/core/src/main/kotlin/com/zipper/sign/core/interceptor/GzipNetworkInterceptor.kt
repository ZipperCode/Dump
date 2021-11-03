package com.zipper.sign.core.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream

class GzipNetworkInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        val body = response.body
        val result = if (body != null && response.header("Content-Encoding") == "gzip") {
            val mediaType: MediaType? = body.contentType()
            var data = body.bytes()
            data = uncompress(data)
            //創建一個新的responseBody，返回進行處理
            response.newBuilder()
                    .body(data.toResponseBody(mediaType))
                    .build()
        } else {
            response
        }
        return result
    }

    /**
     * 字節數組解壓
     */    private fun uncompress(bytes: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        val `in` = ByteArrayInputStream(bytes)
        var gzipInputStream: GZIPInputStream? = null
        try {
            gzipInputStream = GZIPInputStream(`in`)
            val buffer = ByteArray(4096)
            var n: Int
            while (gzipInputStream.read(buffer).also { n = it } >= 0) {
                out.write(buffer, 0, n)
            }
        } catch (e: IOException) {
            println("gzip uncompress error.")
        } finally {
            gzipInputStream?.close()
        }
        return out.toByteArray()
    }
}