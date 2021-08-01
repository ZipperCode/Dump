package com.zipper.core.utils

object StringUtil {

    fun escape(src: String): String {
        var j: Char
        val tmp = StringBuffer()
        tmp.ensureCapacity(src.length * 6)
        var i: Int = 0
        while (i < src.length) {
            j = src[i]
            if (Character.isDigit(j) || Character.isLowerCase(j)
                || Character.isUpperCase(j)
            ) tmp.append(j) else if (j.toInt() < 256) {
                tmp.append("%")
                if (j.toInt() < 16) tmp.append("0")
                tmp.append(Integer.toString(j.toInt(), 16))
            } else {
                tmp.append("%u")
                tmp.append(Integer.toString(j.toInt(), 16))
            }
            i++
        }
        return tmp.toString()
    }

    fun unescape(src: String): String {
        val tmp = StringBuffer()
        tmp.ensureCapacity(src.length)
        var lastPos = 0
        var pos = 0
        var ch: Char
        while (lastPos < src.length) {
            pos = src.indexOf("%", lastPos)
            if (pos == lastPos) {
                if (src[pos + 1] == 'u') {
                    ch = src
                        .substring(pos + 2, pos + 6).toInt(16).toChar()
                    tmp.append(ch)
                    lastPos = pos + 6
                } else {
                    ch = src
                        .substring(pos + 1, pos + 3).toInt(16).toChar()
                    tmp.append(ch)
                    lastPos = pos + 3
                }
            } else {
                lastPos = if (pos == -1) {
                    tmp.append(src.substring(lastPos))
                    src.length
                } else {
                    tmp.append(src.substring(lastPos, pos))
                    pos
                }
            }
        }
        return tmp.toString()
    }

    fun getPathFileName(str:String): String{
        val start = str.lastIndexOf("/")
        val end = str.indexOf(".")
        return str.substring(start + 1, end)
    }
}