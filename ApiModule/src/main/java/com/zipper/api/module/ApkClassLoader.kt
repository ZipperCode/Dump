package com.zipper.api.module

import dalvik.system.BaseDexClassLoader
import dalvik.system.PathClassLoader
import java.lang.Exception
import java.lang.reflect.Field

class ApkClassLoader : PathClassLoader {
    /**
     * 源加载器
     */
    private val mOriginClassloader: ClassLoader

    /**
     * ClassLoader中的父加载器
     */
    private var mParentField: Field? = null

    constructor(dexPath: String?, origin: ClassLoader) : super(dexPath, origin.parent) {
        mOriginClassloader = origin
        injectParent()
    }

    constructor(dexPath: String?, librarySearchPath: String?, origin: ClassLoader) : super(
        dexPath,
        librarySearchPath,
        origin.parent
    ) {
        mOriginClassloader = origin
        injectParent()
    }

    private fun injectParent() {
        try {
            if (mParentField == null) {

                val cls = Class.forName("java.lang.ClassLoader")
                /*
                 * ClassLoader的parent类属性：双亲加载
                 */
                mParentField = ReflectUtils.loadHideField(cls,"parent")
                mParentField!!.isAccessible = true
            }
            mParentField!![mOriginClassloader] = this
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hookElement(){
        try{
            val clsLoaderCls = BaseDexClassLoader::class.java
            val dexPathListCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList")
            val pathListObj = ReflectUtils.loadHideFieldValue(this,"pathList")

            val dexElementsObj = ReflectUtils.loadHideFieldValue(pathListObj,"dexElements") as Array<*>



        }catch (e: Exception){
            e.printStackTrace()
        }


    }
}