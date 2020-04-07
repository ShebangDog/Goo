package com.shebang.dog.goo.data.remote.api.gurumenavi

import com.shebang.dog.goo.model.*

interface GurumenaviApiClient {
    suspend fun fetchGurumenavi(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int,
        dataCount: Int = 5
    ): RestaurantStreet

    suspend fun fetchGurumenavi(
        id: Id
    ): RestaurantData?
}