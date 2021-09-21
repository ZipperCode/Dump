package com.zipper.api.module

data class TaskRunningEvent<T>(
   val moduleKey:String,
   val value: T? = null
) {}