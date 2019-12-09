package com.shebang.dog.goo.data.repository.remote.api

import com.shebang.dog.goo.data.repository.response.Hotpepper
import retrofit2.http.GET
import retrofit2.http.Query

interface HotpepperApi {
    @GET("hotpepper/gourmet/v1/")
    suspend fun fetchHotpepper(
        @Query("key") key: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("range") range: Int,
        @Query("start") index: Int,
        @Query("count") dataCount: Int,
        @Query("format") format: String
    ): Hotpepper

    @GET("hotpepper/gourmet/v1/")
    suspend fun fetchHotpepper(
        @Query("key") key: String,
        @Query("id") id: String,
        @Query("format") format: String
    ): Hotpepper
}