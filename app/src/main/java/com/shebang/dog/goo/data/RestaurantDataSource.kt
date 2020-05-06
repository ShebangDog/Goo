package com.shebang.dog.goo.data

import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import kotlinx.coroutines.flow.Flow

interface RestaurantDataSource {
    fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int = 5
    ): Flow<RestaurantStreet>

    fun fetchRestaurantStreet(): Flow<RestaurantStreet>

    suspend fun fetchRestaurant(id: Id): RestaurantData?

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantStreet: RestaurantStreet)

    fun deleteRestaurants()

    fun deleteRestaurantData(id: Id)
}