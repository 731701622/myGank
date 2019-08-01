package com.wkx.gank.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BaseResult<T>(val error: Boolean, val results: List<T>)

@Parcelize
data class Gank(
    @SerializedName("_id") val id: String,
    val desc: String,
    val url: String,
    val type: String,
    val source: String,
    val createdAt: String,
    val publishedAt: String,
    val who: String?
) : Parcelable

@Parcelize
data class History(
    @SerializedName("_id") val id: String,
    val content: String,
    @SerializedName("created_at") val createdAt: String,
    val publishedAt: String,
    @SerializedName("rand_id") val randId: String,
    val title: String,
    @SerializedName("updated_at") val updatedAt: String,
    var cover: String? = ""
) : Parcelable

enum class RefreshStatus {
    START, END
}
