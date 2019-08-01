package com.wkx.gank.ui.home.adapter

import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.wkx.common.adapter.BaseMultiRecyclerViewAdapter
import com.wkx.common.adapter.BaseViewHolder
import com.wkx.gank.R
import com.wkx.gank.entity.Gank
import com.wkx.gank.utils.GlideApp

class HomeAdapter : BaseMultiRecyclerViewAdapter<Gank>() {

    override fun addItemViewType(position: Int): Int {
        return if (getItem(position).type == "福利") {
            R.layout.item_recycler_image
        } else {
            R.layout.item_recycler_content
        }
    }

    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            R.layout.item_recycler_image -> BaseViewHolder.create(parent, R.layout.item_recycler_image)
            R.layout.item_recycler_content -> BaseViewHolder.create(parent, R.layout.item_recycler_content)
            else -> throw IllegalStateException("")
        }
    }

    override fun convert(holder: BaseViewHolder, item: Gank, position: Int) {
        when (holder.itemViewType) {
            R.layout.item_recycler_image -> {
                holder.setImage(R.id.item_image) {
                    GlideApp.with(holder.itemView.context)
                        .load(item.url)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getView(R.id.item_image))
                }
            }
            R.layout.item_recycler_content -> {
                holder.setText(R.id.item_desc, item.desc)
                    .setText(R.id.item_type, item.type)
                    .setText(R.id.item_who, item.who)
                    .setText(R.id.item_date, item.createdAt.substring(0, 10))
            }
        }
    }
}