package com.shebang.dog.goo.repository.local

import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.repository.RestaurantDataSource

class RestaurantLocalDataSource(private val restaurantDatabase: RestaurantDatabase) :
    RestaurantDataSource {
    private val restaurantDao = restaurantDatabase.restaurantDao()

    override suspend fun getRestaurants(): RestaurantStreet {
        return RestaurantStreet(restaurantDao.getRestaurantStreet().orEmpty())
    }

    override suspend fun getRestaurant(id: Id): RestaurantData? {
        return restaurantDao.getRestaurantData(id)
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        restaurantDao.insertRestaurantData(restaurantData)
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        restaurantStreet.restaurantDataList.forEach {
            restaurantDao.insertRestaurantData(it)
        }
    }

    override fun deleteRestaurants() {
        restaurantDao.deleteRestaurantStreet()
    }
}