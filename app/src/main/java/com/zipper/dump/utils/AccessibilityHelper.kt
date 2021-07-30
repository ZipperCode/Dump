package com.zipper.dump.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import com.zipper.core.utils.L
import com.zipper.dump.bean.AppInfo
import com.zipper.dump.bean.ViewInfo
import com.zipper.dump.repo.AppsRepo
import com.zipper.dump.repo.ServiceRepo
import com.zipper.dump.room.DBHelper
import com.zipper.dump.service.DumpService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object AccessibilityHelper {
    private val TAG: String = AccessibilityHelper::class.java.simpleName

    private const val SP_PKS_LIST_KEY = "name_list"

    /**
     * 保存需要过滤的包名
     */
    private val mNameList: MutableSet<String> = HashSet()

    /**
     * 本地保存选中跳过的view
     */
    private val mDumpViewInfo: MutableList<ViewInfo> = ArrayList()

    /**
     * 无障碍服务收集的view信息 {@link }
     */
    var mCollectViewInfoList: MutableList<ViewInfo>? = null

    /**
     * 已安装的App信息
     */
    val mMainAppInfo: MutableList<AppInfo> = ArrayList()

    /**
     * 是否处于绘制视图时刻，此时不需要捕获任何无障碍事件
     */
    var mDrawViewBound: Boolean = false

    var isInit: Boolean = false

    private val appRepo = AppsRepo()

    val serviceRepo = ServiceRepo()

    private val mDumpPksInfo = mutableSetOf<String>()

    private val mInitMutex =  Mutex()

    val serviceStatusFlow: Flow<Boolean> = serviceRepo.serviceCtrlState

    suspend fun init(context: Context) {
        if(isInit) return
        mInitMutex.withLock {
            if(isInit) return
            try {
                pksInit(context)
                viewInfoInit(context)
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
            isInit = true
        }
    }

    private suspend fun pksInit(context: Context){
        appRepo.loadSaveDumpPksInfo()
        appRepo.savePksInfo.collect {
            mDumpPksInfo.clear()
            mDumpPksInfo.addAll(it)
        }
    }

    suspend fun addPks(pks: String) {
        mDumpPksInfo.add(pks)
        appRepo.putSaveDumpPksInfo(mDumpPksInfo)
    }

    suspend fun addPks(pks: Collection<String>){
        mDumpPksInfo.addAll(pks)
        appRepo.putSaveDumpPksInfo(mDumpPksInfo)
    }

    suspend fun delPks(pks: String){
        mDumpPksInfo.remove(pks)
        appRepo.putSaveDumpPksInfo(mDumpPksInfo)
    }

    suspend fun clearPks() {
        mDumpPksInfo.clear()
        appRepo.putSaveDumpPksInfo(emptySet())
    }

    fun pksContains(pks: String): Boolean = mNameList.contains(pks)

    fun pksContainsAll(pks: List<String>): Boolean = mNameList.containsAll(pks)


    suspend fun closeServiceCtrl(){
        serviceRepo.saveServiceCtrlState(false)
    }

    /**
     * 从数据库中查找所有保存的view信息
     */
    private suspend fun viewInfoInit(context: Context) {
        val db = DBHelper.openViewInfoDatabase(context.applicationContext)
        val firstIn = appRepo.firstInStatus()
        if(!firstIn){
            val initViewInfo = readConfigViewInfo(context.applicationContext)
            db.getViewInfoDao().insert(*initViewInfo.toTypedArray())
            appRepo.saveFirstInStatus(true)
        }
        try {
            mDumpViewInfo.clear()
            mDumpViewInfo.addAll(db.getViewInfoDao().getAll())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun readConfigViewInfo(context: Context): List<ViewInfo> = withContext(Dispatchers.IO) {
        val result: MutableList<ViewInfo> = ArrayList()
        try {
            val prop = Properties()
            prop.load(context.assets.open("dump_ids.ini"))
            prop.forEach {
                it.key
            }
            val propertyNames = prop.propertyNames()
            while (propertyNames.hasMoreElements()) {
                val key = propertyNames.nextElement() as String
                val viewIdString = prop[key] as String
                val viewIds = viewIdString.split(",")
                for (id in viewIds) {
                    result.add(ViewInfo(0, key, id, Rect()))
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return@withContext result
    }

    fun addViewInfo(viewInfo: ViewInfo) {
        mDumpViewInfo.add(viewInfo)
        val dao = DBHelper.getViewInfoDao()
        try {
            dao.insert(viewInfo)
            mDumpViewInfo.add(viewInfo)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun deleteViewInfo(viewInfo: ViewInfo) {
        val dao = DBHelper.getViewInfoDao()
        try {
            dao.delete(viewInfo)
            mDumpViewInfo.remove(viewInfo)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun viewInfoListIds(packageName: String): List<String> {
        if (TextUtils.isEmpty(packageName)) {
            return emptyList()
        }
        return mDumpViewInfo
            .filter { (it.packageName == packageName) and !TextUtils.isEmpty(it.viewId) }
            .map { it.viewId!! }.distinct()

    }

    fun packageViewInfoList(packageName: String): List<ViewInfo> {
        if (TextUtils.isEmpty(packageName)) {
            return emptyList()
        }
        try {
            return DBHelper.getViewInfoDao().queryByPackageName(packageName)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    /**
     * 点击
     * @param nodeInfo      待点击的节点
     */
    fun click(nodeInfo: AccessibilityNodeInfo): Boolean {
        return performAction(nodeInfo, AccessibilityNodeInfo.ACTION_CLICK)
    }

    /**
     * 深入点击，当前控件不可点击，就查找可点击的父控件
     */
    fun deepClick(nodeInfo: AccessibilityNodeInfo): Boolean {
        var isClick = nodeInfo.isClickable
        try {
            if (!isClick) {
                var canClick = nodeInfo
                do {
                    canClick = findClickableView(canClick)
                    isClick = click(canClick)
                } while (!isClick)
            } else {
                click(nodeInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DumpService.mAccessibilityService.let {
                    val rect = Rect()
                    nodeInfo.getBoundsInScreen(rect)
                    gestureClick(it, getRandomPath(rect))
                }
            }
        }
        return isClick
    }

    private fun findClickableView(childNode: AccessibilityNodeInfo): AccessibilityNodeInfo {
        L.d(TAG, "[findClickableView] child-clickable = ${childNode.isClickable}")
        return if (!childNode.isClickable and (childNode.parent != null)) {
            findClickableView(childNode.parent)
        } else {
            childNode
        }
    }

    /**
     * 进行手势滑动操作，需要Android N以上版本
     * @param service       无障碍服务
     * @param path          滑动路径
     * @param startTime     持续时间，手势到笔画的时间（毫秒）
     * @param duration      path路径走过的时间（毫秒）
     */

    @RequiresApi(Build.VERSION_CODES.N)
    fun gestureScroll(
        service: AccessibilityService?,
        path: Path,
        @IntRange(from = 0) startTime: Long = 10,
        @IntRange(from = 0) duration: Long = 10,
        callback: AccessibilityService.GestureResultCallback?
    ) {
        if (service == null) {
            return
        }
        L.d(TAG, "[gestureScroll]")
        service.dispatchGesture(
            GestureDescription.Builder()
                .addStroke(
                    GestureDescription.StrokeDescription(path, startTime, duration)
                ).build(),
            callback,
            null
        )
    }

    /**
     * 手势点击 需要Android N以上版本
     * @param service       无障碍服务
     * @param point         触摸点，在屏幕中的位置
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun gestureClick(service: AccessibilityService?, point: Point) {
        L.d(TAG, "[gestureClick] point = $point")
        val pointPth = Path().apply {
            moveTo(point.x.toFloat(), point.y.toFloat())
            lineTo(point.x.toFloat(), point.y.toFloat())
        }
        gestureClick(service, pointPth)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun gestureClick(service: AccessibilityService?, pointPth: Path) {
        L.d(TAG, "[gestureClick] pointPth = $pointPth")
        gestureScroll(
            service,
            pointPth,
            callback = object : AccessibilityService.GestureResultCallback() {})

    }


    fun gestureClick(service: AccessibilityService?, nodeInfo: AccessibilityNodeInfo?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (nodeInfo == null) {
                return
            }
            val rect = Rect()
            nodeInfo.getBoundsInScreen(rect)
            val pointPth = getRandomPath(rect)
            L.d(TAG, "[gestureClick] pointPth = $pointPth")
            gestureScroll(
                service,
                pointPth,
                callback = object : AccessibilityService.GestureResultCallback() {})
        }
    }


    /**
     * 执行其他的操作
     * @param nodeInfo      待操作的节点
     * @param action        执行的操作
     */
    fun performAction(nodeInfo: AccessibilityNodeInfo, action: Int): Boolean {
        L.d(TAG, "[performAction] action = $action")
        try {
            return nodeInfo.performAction(action)
        } finally {
            nodeInfo.recycle()
        }
    }

    /**
     * 通过id查找对应的节点信息
     * @param rootNodeInfo  根节点
     * @param id            查找的id
     */
    fun findNodeById(
        rootNodeInfo: AccessibilityNodeInfo?,
        id: String
    ): AccessibilityNodeInfo? {
        L.d(TAG, "[findNodeById] id = $id")
        if (TextUtils.isEmpty(id) or (rootNodeInfo == null)) {
            return null
        }
        val nodeList = rootNodeInfo!!.findAccessibilityNodeInfosByViewId(id)
        L.d(TAG, "[findNodeById] nodeList = $nodeList")
        return nodeList?.distinct()?.firstOrNull()
    }

    /**
     * 通过文本查找节点
     * @param rootNodeInfo  根节点
     * @param text          文本
     */
    fun findNodeByText(
        rootNodeInfo: AccessibilityNodeInfo?,
        text: String
    ): AccessibilityNodeInfo? {
        L.d(TAG, "[findNodeByText] text = $text")
        if ((rootNodeInfo == null) or TextUtils.isEmpty(text)) {
            return null
        }

        val nodeList = rootNodeInfo!!.findAccessibilityNodeInfosByText(text)
        L.d(TAG, "[findNodeByText] nodeList = $nodeList")
        return nodeList?.distinct()?.firstOrNull()
    }

    fun collectViewInfo(
        viewNodeInfo: AccessibilityNodeInfo?,
        viewInfoList: MutableList<ViewInfo>
    ) {
        if (viewNodeInfo == null) {
            return
        }
        val screenRect = Rect()
        viewNodeInfo.refresh()
        viewNodeInfo.getBoundsInScreen(screenRect)
        viewInfoList.add(
            ViewInfo(
                0,
                viewNodeInfo.packageName.toString(),
                viewNodeInfo.viewIdResourceName,
                screenRect
            )
        )
        for (i in 0 until viewNodeInfo.childCount) {
            val child = viewNodeInfo.getChild(i)
            collectViewInfo(child, viewInfoList)
        }
    }

    fun getRandomPath(rect: Rect): Path {
        L.d(TAG, "[getRandomPath] rect = $rect")
        val path = Path()
        val point =
            Point(getRandomPoint(rect.left, rect.right), getRandomPoint(rect.top, rect.bottom))
        path.moveTo(point.x.toFloat(), point.y.toFloat())
        L.d(TAG, rect.toString())
        L.d(TAG, point.toString())
        return path
    }

    private fun getRandomPoint(start: Int, end: Int): Int {
        return (Math.random() * (end - start) + start).toInt()
    }
}