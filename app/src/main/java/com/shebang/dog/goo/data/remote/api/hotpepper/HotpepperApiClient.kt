package com.shebang.dog.goo.data.remote.api.hotpepper

import com.shebang.dog.goo.model.*

interface HotpepperApiClient {
    suspend fun fetchHotpepper(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int,
        dataCount: Int = 5,
        format: Format = Format.Json
    ): RestaurantStreet

    suspend fun fetchHotpepper(
        id: Id,
        format: Format = Format.Json
    ): RestaurantData?
}