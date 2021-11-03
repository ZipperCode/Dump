package com.zipper.sign.core

import com.zipper.sign.core.util.Base64
import com.zipper.sign.core.util.StringUtil
import java.lang.Exception
import java.security.MessageDigest

fun ByteArray.base64Str(flag: Int = 0): String{
    return Base64.encodeToString(this, flag)
}

fun ByteArray.hex(): String{
    return StringUtil.byteToHexString(this)
}

fun ByteArray.string(): String{
    return String(this)
}

fun ByteArray.md5(): ByteArray{
    return try {
        MessageDigest.getInstance("MD5").digest(this)
    }catch (e: Exception){
        e.printStackTrace()
        ByteArray(0)
    }
}