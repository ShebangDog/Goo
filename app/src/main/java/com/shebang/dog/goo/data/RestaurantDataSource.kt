package com.shebang.dog.goo.data

import com.shebang.dog.goo.model.*
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