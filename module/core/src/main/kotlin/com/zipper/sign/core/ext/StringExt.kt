package com.zipper.sign.core.ext

import com.zipper.sign.core.base64Str
import com.zipper.sign.core.hex
import com.zipper.sign.core.md5
import com.zipper.sign.core.util.Base64
import com.zipper.sign.core.util.StringUtil
import com.zipper.sign.core.util.SymmetricUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.StandardCharsets

enum class EncodeMode{
    HEX,BASE64,NONE
}

fun String.encodeBase64(flag: Int = 0): String{
    return Base64.encodeToString(toByteArray(StandardCharsets.UTF_8),flag)
}

fun String.decodeBase64(flag: Int = 0): ByteArray{
    return Base64.decode(this,flag)
}

fun String.decodeBase64Str(flag: Int = 0): String{
    return String(Base64.decode(this,flag))
}

fun String.hex(): String{
    return StringUtil.byteToHexString(toByteArray(StandardCharsets.UTF_8))
}

fun String.md5(encodeMode: EncodeMode = EncodeMode.BASE64): String{
    val md5Data = toByteArray(StandardCharsets.UTF_8).md5()
    return when(encodeMode){
        EncodeMode.BASE64 -> md5Data.base64Str()
        EncodeMode.HEX -> md5Data.hex()
        EncodeMode.NONE -> String(md5Data)
    }
}

fun String.symmetric(key: String, iv: String = "", isEncrypt: Boolean = true, type: String, formation:String): ByteArray{
    return if (iv.isEmpty()){
        if (isEncrypt){
            SymmetricUtil.encrypt(key.toByteArray(), this, type, formation)
        }else{
            SymmetricUtil.decrypt(key.toByteArray(),toByteArray(), type, formation)
        }
    }else{
        if (isEncrypt){
            SymmetricUtil.encryptCbc(key.toByteArray(),iv.toByteArray(), toByteArray(), type, formation)
        }else{
            SymmetricUtil.decryptCbc(key.toByteArray(),iv.toByteArray(), toByteArray(), type, formation)
        }
    }
}

fun String.des(key: String, isEncrypt: Boolean = true): ByteArray{
    return symmetric(key, "", isEncrypt, "DES","DES/ECB/PKCS5Padding")
}

fun String.des(key: String, iv: String, isEncrypt: Boolean = true): ByteArray{
    return symmetric(key, iv, isEncrypt, "DES","DES/CBC/PKCS5Padding")
}

fun String.aes(key: String, isEncrypt: Boolean = true): ByteArray{
    return symmetric(key, "", isEncrypt, "AES","AES/ECB/PKCS5Padding")
}

fun String.aes(key: String, iv: String = "", isEncrypt: Boolean = true): ByteArray{
    return symmetric(key, iv, isEncrypt, "AES","AES/CBC/PKCS5Padding")
}

fun String.aes(key: String, isEncrypt: Boolean = true, encodeMode: EncodeMode = EncodeMode.BASE64): String{
    val cryptData = aes(key, isEncrypt)
    return when(encodeMode){
        EncodeMode.BASE64 -> cryptData.base64Str()
        EncodeMode.HEX -> cryptData.hex()
        EncodeMode.NONE -> String(cryptData)
    }
}

fun String.aes(key: String, iv: String, isEncrypt: Boolean = true, encodeMode: EncodeMode = EncodeMode.BASE64): String{
    val cryptData = aes(key, iv, isEncrypt)
    return when(encodeMode){
        EncodeMode.BASE64 -> cryptData.base64Str()
        EncodeMode.HEX -> cryptData.hex()
        EncodeMode.NONE -> String(cryptData)
    }
}

fun String.toJsonRequestBody(): RequestBody {
    return toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

