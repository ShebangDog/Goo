package com.shebang.dog.goo.repository

import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

class RestaurantRepository(
    private val restaurantLocalDataSource: RestaurantDataSource
) : RestaurantDataSource {
    override suspend fun getRestaurants(): RestaurantStreet {
        return restaurantLocalDataSource.getRestaurants()
    }

    override suspend fun getRestaurant(id: Id): RestaurantData? {
        return restaurantLocalDataSource.getRestaurant(id)
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        restaurantLocalDataSource.saveRestaurant(restaurantData)
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        restaurantLocalDataSource.saveRestaurants(restaurantStreet)
    }

    override fun deleteRestaurants() {
        restaurantLocalDataSource.deleteRestaurants()
    }
}