package com.zipper.auto.api.net.cookie

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.utils.SpUtil
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.internal.toHostHeader
import java.io.File
import java.lang.Exception

class PersistentCookieStore(private val storeName: String) : CookieStore {

    companion object{
        const val COOKIE_SP_PREFIX_KEY = "http_cookies"
    }

    private val cookiePersistentMap: MutableMap<String, MutableList<SerializableCookie>> = mutableMapOf()

    private val gson = Gson()

    private val sp = SpUtil.instance(COOKIE_SP_PREFIX_KEY + storeName)

    override fun save(httpUrl: HttpUrl, cookie: Cookie) {
       save(httpUrl, arrayListOf(cookie))
    }

    override fun save(httpUrl: HttpUrl, cookies: List<Cookie>) {
        val key = httpUrl.toHostHeader()
        val saveCookies = cookies.map { SerializableCookie(it)}.toList()
        if (cookiePersistentMap.containsKey(key)) {
            cookiePersistentMap[key]?.addAll(saveCookies)
        } else {
            cookiePersistentMap[key] = cookies.map { SerializableCookie(it) }.distinct().toMutableList()
        }

        val currentCookie = sp.get(key, "[]")

        val serialCookies:  MutableList<SerializableCookie> = gson.fromJson(currentCookie,
            object : TypeToken< MutableList<SerializableCookie>>() {}.type)

        val menCookie = cookiePersistentMap[key]!!
        val resultCookie = margeCookie(menCookie, serialCookies)
        val spValue = gson.toJson(resultCookie)
        sp.put(key, spValue)

    }

    override fun load(httpUrl: HttpUrl): List<Cookie> {
        val key = httpUrl.toHostHeader()
        val currentCookie = sp.get(key, "[]")
        val serialCookie: MutableList<SerializableCookie> = gson.fromJson(currentCookie,
            object : TypeToken<MutableList<SerializableCookie>>() {}.type
        )
        cookiePersistentMap[key] = serialCookie
        return serialCookie.map { it.toCookie() }.toList()
    }

    override fun loadAll(): List<Cookie> {
        cookiePersistentMap.clear()

        val allCookie = sp.all()

        for (cookieEntry in allCookie){
            try{
                val value = cookieEntry.value.toString()
                val serialCookie: MutableList<SerializableCookie> = gson.fromJson(value,
                    object : TypeToken<MutableList<SerializableCookie>>() {}.type
                )
                cookiePersistentMap[cookieEntry.key] = serialCookie
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        return cookiePersistentMap.values.flatten().map { it.toCookie() }.toList()
    }

    override fun removeAll() {
        cookiePersistentMap.clear()
        sp.clear()
    }

    private fun clearExpires(){
        for (cookieEntry in cookiePersistentMap){
            val key = cookieEntry.key
            val cookieValues = cookieEntry.value
            val resultCookie = mutableListOf<SerializableCookie>()

            for (cookie in cookieValues){
                val time = System.currentTimeMillis()
                if(cookie.expiresAt > -1 && time - cookie.expiresAt > 0){
                    resultCookie.add(cookie)
                }
            }

            if(resultCookie.size > 0){
                sp.putAsync(key, resultCookie)
            }
        }
    }

    private fun margeCookie(menCookie: List<SerializableCookie>, localCookie: List<SerializableCookie>): List<SerializableCookie>{
        val mutableList = HashSet<SerializableCookie>()
        mutableList.addAll(menCookie)
        mutableList.addAll(localCookie)
        return mutableList.toList()
    }
}