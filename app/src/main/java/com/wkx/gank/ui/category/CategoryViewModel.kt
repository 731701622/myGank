package com.wkx.gank.ui.category

import androidx.lifecycle.MutableLiveData
import com.wkx.common.base.BaseViewModel
import com.wkx.common.utils.Code
import com.wkx.common.utils.Status
import com.wkx.common.utils.logd
import com.wkx.gank.api.gankService
import com.wkx.gank.entity.Gank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class CategoryViewModel(private val category: String) : BaseViewModel() {

    private var currentPage: Int = 1

    val refreshGankLiveData by lazy { MutableLiveData<List<Gank>>() }

    val loadMoreGankLiveData by lazy { MutableLiveData<List<Gank>>() }

    init {
        refresh()
    }

    fun refresh() = getGank(1)

    fun loadMore() = getGank(currentPage + 1)

    fun retry() = getGank(currentPage + 1)

    fun getGank(page: Int) {
        "getGank page = $page".logd()
        launch {
            if (refreshGankLiveData.value == null) {
                statusLiveData.postValue(Status(Code.ING))
            }
            val list = withContext(Dispatchers.IO) {
                withTimeout(timeoutMillis) {
                    gankService.getGank(category, 20, page).results
                }
            }
            if (list.isNotEmpty()) {
                currentPage = page
                statusLiveData.postValue(Status(Code.SUCCESS))
                if (page == 1) {
                    refreshGankLiveData.postValue(list)
                } else {
                    loadMoreGankLiveData.postValue(list)
                }
            } else {
                statusLiveData.postValue(Status(Code.EMPTY))
            }
        }
    }

}