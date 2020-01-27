package com.shebang.dog.goo.data.repository

import com.shebang.dog.goo.data.model.*

interface RestaurantDataSource {
    suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): RestaurantStreet

    suspend fun fetchRestaurant(id: Id): RestaurantData?

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantStreet: RestaurantStreet)

    fun deleteRestaurants()

    fun deleteRestaurantData(id: Id)
}