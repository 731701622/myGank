package com.wkx.gank.api

import com.wkx.gank.entity.BaseResult
import com.wkx.gank.entity.Gank
import com.wkx.gank.entity.History
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GankService {

    @GET("today")
    suspend fun getToday(): ResponseBody

    @GET("history/content/10/{page}")
    suspend fun getHistory(@Path("page") page: Int): BaseResult<History>

    @GET("day/{datetime}")
    suspend fun getGank(@Path("datetime") datetime: String): ResponseBody

    @GET("data/{category}/{count}/{page}")
    suspend fun getGank(
        @Path("category") category: String,
        @Path("count") count: Int,
        @Path("page") page: Int
    ): BaseResult<Gank>

}

private const val API_BASE_URL = "https://gank.io/api/"

val gankService: GankService by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    return@lazy retrofit.create(GankService::class.java)
}