package com.zipper.api.module

import com.zipper.core.utils.L
import dalvik.system.BaseDexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Field

class ProxyClassLoader : PathClassLoader {

    companion object{
        const val TAG: String = "ModuleClassLoader"

        fun hookElement(moduleDexList: List<String>, loader: ClassLoader){
            try {
                val dexPathListCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList")
                val elementCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList\$Element")
                val baseDexClassLoaderCls = BaseDexClassLoader::class.java
                val pathListField = ReflectUtils.loadHideField(baseDexClassLoaderCls, "pathList")
                val pathListObj = pathListField.get(loader)
                val makeDexElementsMethod = ReflectUtils.loadHideMethod(dexPathListCls,"makeDexElements",
                    List::class.java, File::class.java, List::class.java, ClassLoader::class.java)

                val dexElementsField = ReflectUtils.loadHideField(dexPathListCls, "dexElements") as Field

                val oldDexElements = ReflectUtils.loadHideFieldValue(pathListObj,"dexElements") as Array<*>

                val moduleDexFileList = mutableListOf<File>()
                for (moduleDexPath in moduleDexList){
                    val file = File(moduleDexPath)
                    if(!file.exists()){
                        L.d(TAG, "dexFile ${file.absoluteFile} not exists")
                        continue
                    }
                    moduleDexFileList.add(file)
                }

                val expList = mutableListOf<IOException>()
                val newElements = makeDexElementsMethod.invoke(pathListObj, moduleDexFileList, null, expList, null) as Array<*>

                val newElementLen = oldDexElements.size + newElements.size
                val dexElements = java.lang.reflect.Array.newInstance(elementCls, newElementLen)

                System.arraycopy(newElements, 0, dexElements,0, newElements.size)
                System.arraycopy(oldDexElements, 0, dexElements, newElements.size, oldDexElements.size)

                dexElementsField.set(pathListObj,dexElements)

                if(expList.size > 0){
                    expList.forEach {
                        it.printStackTrace()
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /**
     * 源加载器
     */
    private val mOriginClassloader: ClassLoader

    /**
     * ClassLoader中的父加载器
     */
    private var mParentField: Field? = null

    constructor(origin: ClassLoader) : super("", origin.parent) {
        mOriginClassloader = origin
        injectParent()
    }

    constructor(librarySearchPath: String?, origin: ClassLoader) : super(
        "",
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

    fun addDexPath(moduleDexList: List<String>){
        try {
            val dexPathListCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList")
            val baseDexClassLoaderCls = BaseDexClassLoader::class.java
            val pathListField = ReflectUtils.loadHideField(baseDexClassLoaderCls, "pathList")
            val pathListObj = pathListField.get(this)
            val addDexPathMethod = ReflectUtils.loadHideMethod(dexPathListCls, "addDexPath", String::class.java, File::class.java)

            for (moduleDexPath in moduleDexList){
                val file = File(moduleDexPath)
                if(!file.exists()){
                    L.d(TAG, "dexFile ${file.absoluteFile} not exists")
                    continue
                }
                try {
                    addDexPathMethod.invoke(pathListObj, file.absolutePath, null)
                    L.d(TAG,"dexFile ${file.absoluteFile} 成功添加到DexPathList")
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun addDexPath1(moduleDexList: List<String>){
        try {
            val dexPathListCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList")
            val elementCls = ReflectUtils.loadHideForName("dalvik.system.DexPathList\$Element")
            val baseDexClassLoaderCls = BaseDexClassLoader::class.java
            val pathListField = ReflectUtils.loadHideField(baseDexClassLoaderCls, "pathList")
            val pathListObj = pathListField.get(this)
            val makeDexElementsMethod = ReflectUtils.loadHideMethod(dexPathListCls,"makeDexElements",
                List::class.java, File::class.java, List::class.java, ClassLoader::class.java)

            val dexElementsField = ReflectUtils.loadHideField(dexPathListCls, "dexElements") as Field

            val oldDexElements = ReflectUtils.loadHideFieldValue(pathListObj,"dexElements") as Array<*>

            val moduleDexFileList = mutableListOf<File>()
            for (moduleDexPath in moduleDexList){
                val file = File(moduleDexPath)
                if(!file.exists()){
                    L.d(TAG, "dexFile ${file.absoluteFile} not exists")
                    continue
                }
                moduleDexFileList.add(file)
            }

            val expList = mutableListOf<IOException>()
            val newElements = makeDexElementsMethod.invoke(pathListObj, moduleDexFileList, null, expList, null) as Array<*>

            val newElementLen = oldDexElements.size + newElements.size
            val dexElements = java.lang.reflect.Array.newInstance(elementCls, newElementLen)

            System.arraycopy(newElements, 0, dexElements,0, newElements.size)
            System.arraycopy(oldDexElements, 0, dexElements, newElements.size, oldDexElements.size)

            dexElementsField.set(pathListObj,dexElements)

            if(expList.size > 0){
                expList.forEach {
                    it.printStackTrace()
                }
            }
        }catch (e: Exception) {
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