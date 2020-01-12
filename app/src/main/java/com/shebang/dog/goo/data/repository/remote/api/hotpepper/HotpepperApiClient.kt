package com.shebang.dog.goo.data.repository.remote.api.hotpepper

import com.shebang.dog.goo.data.model.*

interface HotpepperApiClient {
    suspend fun fetchHotpepper(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int = 1,
        dataCount: Int = 10,
        format: Format = Format.Json
    ): RestaurantStreet

    suspend fun fetchHotpepper(
        id: Id,
        format: Format = Format.Json
    ): RestaurantData?
}