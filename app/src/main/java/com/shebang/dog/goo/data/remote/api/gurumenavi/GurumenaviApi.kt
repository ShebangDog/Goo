package com.shebang.dog.goo.data.remote.api.gurumenavi

import com.shebang.dog.goo.data.response.gurumenavi.Gurumenavi
import retrofit2.http.GET
import retrofit2.http.Query

interface GurumenaviApi {
    @GET("RestSearchAPI/v3/")
    suspend fun fetchGurumenavi(
        @Query("keyid") keyId: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("range") range: Int,
        @Query("offset_page") index: Int,
        @Query("hit_per_page") dataCount: Int
    ): Gurumenavi

    @GET("RestSearchAPI/v3/")
    suspend fun fetchGurumenavi(
        @Query("keyid") keyId: String,
        @Query("id") id: String
    ): Gurumenavi
}