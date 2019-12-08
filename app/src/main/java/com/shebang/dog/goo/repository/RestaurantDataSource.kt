package com.shebang.dog.goo.repository

import com.shebang.dog.goo.model.*

interface RestaurantDataSource {
    suspend fun fetchRestaurantStreet(
        latitude: Latitude,
        longitude: Longitude,
        range: Range
    ): FindData<RestaurantStreet>

    suspend fun fetchRestaurant(id: Id): FindData<RestaurantData>

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantStreet: RestaurantStreet)

    fun deleteRestaurants()

    fun deleteRestaurantData(id: Id)
}