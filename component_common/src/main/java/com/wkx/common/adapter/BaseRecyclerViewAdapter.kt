package com.wkx.common.adapter

import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T> @JvmOverloads constructor(
    private val itemLayoutRes: Int, list: ArrayList<T> = ArrayList()
) : BaseMultiRecyclerViewAdapter<T>(list) {

    override fun addItemViewType(position: Int): Int = TYPE_ITEM

    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.create(parent, itemLayoutRes)
    }
}