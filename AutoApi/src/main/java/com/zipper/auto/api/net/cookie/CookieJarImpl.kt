package com.zipper.auto.api.net.cookie
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl(private val cookieStore: CookieStore) : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.load(url)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.save(url, cookies)
    }

    companion object{
        val NO_COOKIES = CookieJarImpl(object :CookieStore{
            override fun save(httpUrl: HttpUrl, cookies: List<Cookie>) {
            }

            override fun load(httpUrl: HttpUrl): List<Cookie> {
                return emptyList()
            }

            override fun loadAll(): List<Cookie> {
                return emptyList()
            }

            override fun removeAll() {
            }

            override fun save(httpUrl: HttpUrl, cookie: Cookie) {

            }
        })
    }

}