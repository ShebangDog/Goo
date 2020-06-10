package com.shebang.dog.goo.data.remote.api.hotpepper

import com.shebang.dog.goo.data.model.location.Latitude
import com.shebang.dog.goo.data.model.location.Longitude
import com.shebang.dog.goo.data.model.query.Format
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData

interface HotpepperApiClient {
    suspend fun fetchHotpepper(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int,
        dataCount: Int = 5,
        format: Format = Format.Json
    ): List<RestaurantData>

    suspend fun fetchHotpepper(
        id: Id,
        format: Format = Format.Json
    ): RestaurantData?
}