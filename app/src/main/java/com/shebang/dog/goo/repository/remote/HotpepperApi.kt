package com.shebang.dog.goo.repository.remote

import com.shebang.dog.goo.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface HotpepperApi {
    @GET("/hotpepper/gourmet/v1/")
    fun getRestaurantStreet(
        @Query("key") key: String,
        @Query("lat") latitude: Latitude,
        @Query("lng") longitude: Longitude,
        @Query("range") range: Range,
        @Query("start") index: Int = 1,
        @Query("count") dataCount: Int = 10,
        @Query("format") format: Format = Format.Json
    ): RestaurantStreet

    @GET("/hotpepper/gourmet/v1/")
    fun getRestaurant(
        @Query("key") key: String,
        @Query("id") id: Id,
        @Query("format") format: Format = Format.Json
    ): RestaurantStreet
}