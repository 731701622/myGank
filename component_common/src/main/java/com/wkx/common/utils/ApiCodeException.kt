package com.wkx.common.utils

import android.accounts.NetworkErrorException
import android.util.Log
import android.util.MalformedJsonException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class ApiCodeException(errorMsg: String) : Exception(errorMsg) {

    companion object {
        fun checkException(e: Throwable): Status {
            Log.e("zz", e.message, e)
//            return when {
//                e.message?.contains("No address associated with hostname") ?: false -> Status(Code.NO_NET)
//                e is ConnectException || e is SocketTimeoutException
//                        || e is NetworkErrorException
//                        || e is TimeoutException -> Status(Code.NO_NET, "网络连接错误，请检查网络")
//                e is MalformedJsonException || e is JsonSyntaxException -> Status(Code.ERROR, "解析Json异常")
//                e is ApiCodeException -> Status(Code.ERROR, e.message ?: "empty message")
//                else -> Status(Code.ERROR, "未知错误")
//            }
            val msg = when {
                e is TimeoutCancellationException -> e.message
                e.message?.contains("No address associated with hostname") ?: false -> "无网络连接"
                e is ConnectException || e is SocketTimeoutException
                        || e is NetworkErrorException
                        || e is TimeoutException -> "网络连接错误，请检查网络"
                e is MalformedJsonException || e is JsonSyntaxException -> "数据解析异常(Json Error)"
                e is ApiCodeException -> e.message ?: "empty message"
                else -> "未知错误"
            }
            return Status(Code.ERROR, msg)
        }
    }
}