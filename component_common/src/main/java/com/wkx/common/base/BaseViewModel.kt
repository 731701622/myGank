package com.wkx.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wkx.common.utils.ApiCodeException
import com.wkx.common.utils.Code
import com.wkx.common.utils.Status
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel()/*, CoroutineScope by MainScope()*/ {

    val timeoutMillis = 10_000L

    open val statusLiveData by lazy { MutableLiveData<Status>() }

    fun <T> launch(block: suspend CoroutineScope.() -> T) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            statusLiveData.value = ApiCodeException.checkException(e)
            e.printStackTrace()
        } finally {
            statusLiveData.value = Status(Code.END)
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

}