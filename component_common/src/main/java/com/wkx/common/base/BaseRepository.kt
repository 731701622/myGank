package com.wkx.common.base

abstract class BaseRepository {

    suspend fun <T : Any> call(call: suspend () -> T): T {
        return call.invoke()
    }

}