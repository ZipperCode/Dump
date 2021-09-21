package com.zipper.api.module

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import com.zipper.core.activity.BaseActivity
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.RuntimeException

/**
 *
 * @author zhangzhipeng
 * @date   2021/9/15
 **/
class ApiModuleContext(base: Context,moduleFile: File): ContextWrapper(base) {

    /** 插件资源类 */
    private val mResources: Resources
    /** 插件的类加载器 */
    private var mPluginClassLoader: ClassLoader
    /** 插件包信息 */
    private val mApplicationInfo: ApplicationInfo
    /** 插件包名 */
    private val mPackageName: String
    /** 包资源路径 */
    private val mPackageResourcePath:String

    private var noCompatScaledDensity = 0.0f
    private var noCompatDensity = 0.0f
    private var screenOrientation = false

    init {
        if (!moduleFile.exists()) {
            throw RuntimeException("not found resource apk, init PluginContext error")
        }
        // 插件类加载器
        mPluginClassLoader = PathClassLoader(moduleFile.absolutePath, baseContext.classLoader)
        val packageArchiveInfo = packageManager.getPackageArchiveInfo(moduleFile.absolutePath, 0)
            ?: throw RuntimeException("not found packageArchiveInfo ${moduleFile.absolutePath} 模块包不存在")
        // 插件包信息
        mApplicationInfo = packageArchiveInfo.applicationInfo
        mApplicationInfo.publicSourceDir = moduleFile.absolutePath
        mApplicationInfo.sourceDir = moduleFile.absolutePath
        mPackageResourcePath = moduleFile.absolutePath
        mResources = packageManager.getResourcesForApplication(mApplicationInfo)
        mPackageName = mApplicationInfo.packageName

        noCompatScaledDensity = resources.displayMetrics.scaledDensity
        noCompatDensity = resources.displayMetrics.density
        screenOrientation =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        autoSize()
    }

    private fun autoSize() {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val screenWidthPixels = displayMetrics.widthPixels
        // px = density * dp, density = dpi / 160 <==> dpi = density * 160
        val targetDensity =
            (screenWidthPixels * 1.0f) / if (screenOrientation) BaseActivity.DESIGN_UI_HEIGHT_DP else BaseActivity.DESIGN_UI_WIDTH_DP
        val targetScaledDensity = targetDensity * (noCompatScaledDensity / noCompatDensity)
        val targetDensityDpi = (targetDensity * 160).toInt()
        displayMetrics.apply {
            density = targetDensity
            densityDpi = targetDensityDpi
            scaledDensity = targetScaledDensity
        }
    }

    override fun getAssets(): AssetManager {
        return resources.assets
    }

    override fun getResources(): Resources {
        return mResources
    }

    override fun getTheme(): Theme {
        return resources.newTheme()
    }

    override fun getPackageName(): String {
        return mPackageName
    }

    override fun getApplicationInfo(): ApplicationInfo {
        return mApplicationInfo
    }

    override fun getPackageResourcePath(): String {
        return mPackageResourcePath
    }

    override fun getClassLoader(): ClassLoader {
        return mPluginClassLoader;
    }

    override fun getSystemService(name: String): Any {
        return when(name){
            Context.LAYOUT_INFLATER_SERVICE -> {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    LayoutInflater.from(baseContext).cloneInContext(this)
                } else PhoneLayoutInflater(this)
            }
            else ->{
                super.getSystemService(name)
            }
        }
    }
}