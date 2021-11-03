package com.zipper.sign.core.interceptor

import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.RealResponseBody
import okio.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream


class GzipResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response =  chain.proceed(originalRequest)
        if (response.body != null && "gzip".equals(response.header("Content-Encoding") , true)){
            val mediaType: MediaType? = response.body!!.contentType()
            var data = response.body!!.bytes()
            data = uncompress(data)
            return response.newBuilder()
                .body(data.toResponseBody(mediaType))
                .build()
        }
        return response
    }

    /** https://github.com/square/okhttp/issues/350  */
    @Throws(IOException::class)
    private fun forceContentLength(requestBody: RequestBody): RequestBody {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return requestBody.contentType()
            }

            override fun contentLength(): Long {
                return buffer.size
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                sink.write(buffer.snapshot())
            }
        }
    }

    private fun gzip(body: RequestBody?): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body!!.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = GzipSink(sink).buffer()
                body!!.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
    private fun uncompress(bytes: ByteArray): ByteArray {
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