package com.shebang.dog.goo.repository

import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

interface RestaurantDataSource {
    interface LoadRestaurantCallback {
        fun onLoad(restaurantStreet: RestaurantStreet)

        fun onFail()
    }

    interface GetRestaurantCallback {
        fun onGet(restaurantData: RestaurantData)

        fun onFail()
    }

    suspend fun loadRestaurants(callback: LoadRestaurantCallback)

    suspend fun loadRestaurant(id: Id, callback: GetRestaurantCallback)

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantStreet: RestaurantStreet)

    fun deleteRestaurants()
}