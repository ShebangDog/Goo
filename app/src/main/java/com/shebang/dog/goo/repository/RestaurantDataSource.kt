package com.shebang.dog.goo.repository

import com.shebang.dog.goo.model.FindData
import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

interface RestaurantDataSource {
    suspend fun fetchRestaurantStreet(): FindData<RestaurantStreet>

    suspend fun fetchRestaurant(id: Id): FindData<RestaurantData>

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantStreet: RestaurantStreet)

    fun deleteRestaurants()
}