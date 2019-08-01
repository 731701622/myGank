package com.wkx.gank.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wkx.common.base.BaseViewModel
import com.wkx.common.utils.ApiCodeException
import com.wkx.common.utils.Code
import com.wkx.common.utils.Status
import com.wkx.gank.api.gankService
import com.wkx.gank.entity.Gank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

class HomeViewModel : BaseViewModel() {

    val todayLiveData by lazy { MutableLiveData<List<Gank>>().also { getToday() } }

    fun refresh() = getToday()

    private fun getToday() {
        launch {
            statusLiveData.postValue(Status(Code.ING))
            val list = withContext(Dispatchers.IO) {
                val responseBody = withTimeout(timeoutMillis) {
                    return@withTimeout gankService.getToday()
                }
                val result = responseBody.string()
                return@withContext parsenJson(result)
            }
            if (list.isNotEmpty()) {
                statusLiveData.postValue(Status(Code.SUCCESS))
                todayLiveData.postValue(list)
            } else {
                statusLiveData.postValue(Status(Code.EMPTY))
            }
        }
    }

    private fun parsenJson(result: String): List<Gank> {
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
            return list
        }
    }

}