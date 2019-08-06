package com.wkx.gank.ui.home.adapter

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.wkx.common.adapter.BaseRecyclerViewAdapter
import com.wkx.common.adapter.BaseViewHolder
import com.wkx.gank.R
import com.wkx.gank.entity.History
import com.wkx.gank.utils.GlideApp

class HistoryAdapter : BaseRecyclerViewAdapter<History>(R.layout.item_recycler_history) {

    override fun convert(holder: BaseViewHolder, item: History, position: Int) {
        holder.setText(R.id.item_date, item.title)
            .setImage(R.id.item_image) {
                GlideApp.with(holder.itemView)
                    .load(item.cover)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(it)
            }
    }
}