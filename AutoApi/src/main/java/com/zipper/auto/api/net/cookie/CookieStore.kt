package com.zipper.auto.api.net.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStore {

    fun save(httpUrl: HttpUrl, cookie: Cookie)

    fun save(httpUrl: HttpUrl, cookies: List<Cookie>)

    fun load(httpUrl: HttpUrl): List<Cookie>

    fun loadAll(): List<Cookie>

    fun removeAll()
}