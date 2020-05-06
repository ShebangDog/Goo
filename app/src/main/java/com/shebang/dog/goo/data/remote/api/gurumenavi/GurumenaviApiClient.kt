package com.shebang.dog.goo.data.remote.api.gurumenavi

import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.data.model.location.Latitude
import com.shebang.dog.goo.data.model.location.Longitude
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData

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