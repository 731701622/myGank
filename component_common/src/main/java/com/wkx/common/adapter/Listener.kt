package com.wkx.common.adapter

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(view: View, item: T, position: Int) {}
}

interface OnLoadMoreListener {

    fun onLoadMore(){}

    fun onRetry(){}
}