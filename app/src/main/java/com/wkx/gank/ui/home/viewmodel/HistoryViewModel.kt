package com.wkx.gank.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.wkx.common.base.BaseViewModel
import com.wkx.common.utils.ApiCodeException
import com.wkx.common.utils.Code
import com.wkx.common.utils.Status
import com.wkx.common.utils.logd
import com.wkx.gank.api.gankService
import com.wkx.gank.entity.Gank
import com.wkx.gank.entity.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

class HistoryViewModel : BaseViewModel() {

    private val pageLiveData by lazy { MutableLiveData<Int>().also { it.value = 1 } }

    val historyLiveData by lazy { MutableLiveData<List<History>>().also { it.value = arrayListOf() } }

    val currentHistoryLiveData by lazy { MutableLiveData<History>() }

    val gankLiveData = Transformations.switchMap(currentHistoryLiveData) {
        return@switchMap getGank(it.publishedAt.substring(0, 10).replace("-", "/"))
    }!!

    init {
        pageLiveData.value?.also {
            getHistory(it)
        }
    }

    private fun getHistory(page: Int) {
        "getHistory page =  $page".logd()
        launch {
            statusLiveData.value = Status(Code.ING)
            val list = withContext(Dispatchers.IO) {
                withTimeout(20_000) {
                    gankService.getHistory(page).results
                }
            }.also {
                it.listIterator().forEach { history ->
                    history.cover = parseSrc(history.content)
                }
            }
            if (currentHistoryLiveData.value == null) {
                currentHistoryLiveData.postValue(list[0])
            }
            historyLiveData.postValue(list)
        }
    }

    private fun getGank(date: String): LiveData<List<Gank>> {
        "getGank() date::$date".logd()
        val liveData = MutableLiveData<List<Gank>>()
        launch {
            statusLiveData.value = Status(Code.ING)
            val list = withContext(Dispatchers.IO) {
                val result = withTimeout(timeoutMillis) {
                    gankService.getGank(date).string()
                }
                val jsonObject = JSONObject(result)
                val error: Boolean = jsonObject.optBoolean("error", true)
                if (error) {
                    throw ApiCodeException("服务器返回异常")
                } else {
                    val categoryArrays: JSONArray = jsonObject.optJSONArray("category")
                    val resultJsonObject = jsonObject.optJSONObject("results")
                    val list = ArrayList<Gank>()
                    for (i in 0 until categoryArrays.length()) {
                        val category = categoryArrays[i].toString()
                        val resultJsonArray: JSONArray = resultJsonObject.optJSONArray(category)
                        for (j in 0 until resultJsonArray.length()) {
                            val data = Gson().fromJson<Gank>(resultJsonArray[j].toString(), Gank::class.java)
                            list.add(data)
                        }
                    }
                    return@withContext list
                }
            }
            if (list.isEmpty()) {
                statusLiveData.value = Status(Code.EMPTY)
            } else {
                statusLiveData.value = Status(Code.SUCCESS)
            }
            liveData.postValue(list)
        }
        return liveData
    }

    fun getGank(history: History) {
        currentHistoryLiveData.value = history
    }

    fun refresh() {
        currentHistoryLiveData.value?.also {
            currentHistoryLiveData.postValue(it)
        } ?: getHistory(1)
    }

    fun loadMore() {
        pageLiveData.value?.also {
            getHistory(it + 1)
        } ?: also { "loadMore pageLiveData.value=${pageLiveData.value}".logd() }
    }

    fun retry() {
        pageLiveData.value?.also {
            getHistory(it)
        }
    }

    private fun parseSrc(content: String): String? {
        // 把封面地址提取出来
        val pattern = """src="([^"]+)"""
        val src = Regex(pattern).find(content)?.value
        return src?.split('\"')?.get(1)
    }

}