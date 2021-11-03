package com.zipper.sign.core

import com.zipper.sign.core.base.BaseApi
import kotlinx.coroutines.Deferred
import java.io.InputStream
import kotlin.jvm.Throws

interface IModuleProvider {

    suspend fun execute(inputStream: InputStream?): List<Deferred<BaseApi>>
}