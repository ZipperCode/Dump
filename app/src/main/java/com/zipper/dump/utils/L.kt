package com.zipper.dump.utils

import android.util.Log
import com.zipper.dump.BuildConfig

object L {

    fun d(tag: String, msg: String){
        if(BuildConfig.DEBUG){
            Log.d(tag,msg)
        }
    }
}