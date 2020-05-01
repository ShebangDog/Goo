package com.shebang.dog.goo.data.remote.api.hotpepper

import com.shebang.dog.goo.model.query.Format
import com.shebang.dog.goo.model.query.Range
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Latitude
import com.shebang.dog.goo.model.location.Longitude
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.model.restaurant.RestaurantData

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