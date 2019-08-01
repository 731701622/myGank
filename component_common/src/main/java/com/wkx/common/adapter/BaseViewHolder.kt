package com.wkx.common.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val mViews: SparseArray<View> = SparseArray()

    fun <T : View> getView(viewId: Int): T {
        var view = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        @Suppress("UNCHECKED_CAST")
        return view as T
    }

    fun setText(viewId: Int, text: String?): BaseViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setImage(viewId: Int, img: (imageView: ImageView) -> Unit): BaseViewHolder {
        img(getView(viewId))
        return this
    }

    companion object {

        fun create(itemView: View): BaseViewHolder {
            return BaseViewHolder(itemView)
        }

        fun create(parent: ViewGroup, @LayoutRes layoutId: Int): BaseViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return create(itemView)
        }
    }

}