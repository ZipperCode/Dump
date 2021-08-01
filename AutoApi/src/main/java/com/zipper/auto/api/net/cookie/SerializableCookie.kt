package com.zipper.auto.api.net.cookie

import okhttp3.Cookie

class SerializableCookie(cookie: Cookie) {
    val name: String = cookie.name
    val value: String = cookie.value
    val expiresAt: Long = cookie.expiresAt
    val domain: String = cookie.domain
    val path: String = cookie.path

    fun toCookie(): Cookie{
        return Cookie.Builder()
            .name(name)
            .value(value)
            .expiresAt(expiresAt)
            .domain(domain)
            .path(path)
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializableCookie

        if (name != other.name) return false
        if (value != other.value) return false
        if (domain != other.domain) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode() ?: 0
        result = 31 * result + (value.hashCode() ?: 0)
        result = 31 * result + (domain.hashCode() ?: 0)
        result = 31 * result + path.hashCode()
        return result
    }


}