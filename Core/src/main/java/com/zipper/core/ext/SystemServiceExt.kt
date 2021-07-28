package com.zipper.core

import android.app.ActivityManager
import android.content.Context

fun Context.activityManager() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager