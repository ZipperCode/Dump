package com.zipper.core.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import java.util.ArrayDeque

@Navigator.Name("fragment")
class FixFragmentNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, fragmentManager, containerId) {

    companion object {
        const val TAG: String = "FixFragmentNavigator"
    }

    private var backStack: ArrayDeque<Int>? = null

//    override fun popBackStack(): Boolean {
//        if (FragmentNavConfigHelper.isDefaultMode) {
//            return super.popBackStack()
//        }else if (FragmentNavConfigHelper.isOnLyTopMode){
//            if(backStack!!.isEmpty()){
//                return false
//            }
//            if (fragmentManager.isStateSaved) {
//                Log.i(
//                    TAG, "Ignoring popBackStack() call: FragmentManager has already"
//                            + " saved its state"
//                )
//                return false
//            }
//
//            fragmentManager.popBackStack(
//                generateBackStackName(backStack!!.size, backStack!!.peekLast()!!),
//                FragmentManager.POP_BACK_STACK_INCLUSIVE
//            )
//            val destId = backStack!!.removeLast()
//            if(FragmentNavConfigHelper.isTopLevelId(destId)){
//                fragmentManager.primaryNavigationFragment
//            }
//        }
//    }

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {
        // 默认模式下使用父类实现
        if (FragmentNavConfigHelper.isDefaultMode) {
            return super.navigate(destination, args, navOptions, navigatorExtras)
        }

        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }

        val ft = fragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        // 当前显示的片段
        val currentFragment = fragmentManager.primaryNavigationFragment
        val currentDestinationId = currentFragment?.findNavController()?.currentDestination?.id

        val tag = destination.id.toString()

        val fragment = if(currentFragment == null){
            val destFragment = genFragment(destination, args)
            ft.replace(containerId, destFragment, tag)
            destFragment
        }else{
            if (FragmentNavConfigHelper.isOnLyTopMode && FragmentNavConfigHelper.isTopLevelId(currentDestinationId)) {
                // 当前模式下，隐藏当前显示的片段
                ft.hide(currentFragment)

                if (FragmentNavConfigHelper.isTopLevelId(destination.id)) {
                    // top -> top
                    // 根据tag查询目标片段是否是隐藏的片段
                    val nextFragment = fragmentManager.findFragmentByTag(tag)
                    if (nextFragment != null) {
                        ft.show(nextFragment)
                        nextFragment
                    } else {
                        // 目标片段不存在，则加入
                        val frag = genFragment(destination, args)
                        ft.add(containerId, frag, tag)
                        frag
                    }
                } else {
                    // top -> other
                    val frag = genFragment(destination, args)
                    ft.add(containerId, frag, tag)
                    frag
                }
            }else{
                // other -> other
                ft.hide(currentFragment)
                var destFragment = fragmentManager.findFragmentByTag(tag)
                if(destFragment == null){
                    destFragment = genFragment(destination, args)
                    ft.add(containerId, destFragment, tag)
                }else{
                    ft.show(destFragment)
                }
                destFragment
            }
        }



        // 设置当前片段
        ft.setPrimaryNavigationFragment(fragment)

        @IdRes val destId = destination.id

        // 反射获取回退栈
        if (backStack == null) {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            backStack = field.get(this) as ArrayDeque<Int>
        }

        // 是否是首个片段
        val initialNavigation = backStack!!.isEmpty()
        // 目标是否是自身，并且是singleTop
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && backStack!!.peekLast() == destId)

        // 是否处于顶层的片段
        val isTopLevel = (navOptions != null && !initialNavigation
                && FragmentNavConfigHelper.isTopLevelId(destId))

        val isAdded: Boolean = when {
            initialNavigation -> true
            isSingleTopReplacement -> {
                // Single Top 回退到当前 不加入到backStack
                if (backStack!!.size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
                    fragmentManager.popBackStack(
                        generateBackStackName(backStack!!.size, backStack!!.peekLast()!!),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(generateBackStackName(backStack!!.size, destId))
                }
                false
            }
            isTopLevel -> false
            else -> {
                ft.addToBackStack(generateBackStackName(backStack!!.size + 1, destId))
                true
            }
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            backStack!!.add(destId)
            destination
        } else {
            null
        }
    }

    private fun genFragment(destination: Destination, args: Bundle?): Fragment {
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        val frag = instantiateFragment(
            context, fragmentManager,
            className, args
        )
        frag.arguments = args
        return frag
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}