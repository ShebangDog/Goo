package com.shebang.dog.goo.data.repository.local

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import javax.inject.Inject

class RestaurantLocalDataSource @Inject constructor(private val restaurantDao: RestaurantDao) :
    RestaurantDataSource {

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): RestaurantStreet {

        return RestaurantStreet(
            restaurantDao.getRestaurantList().orEmpty()
                .filter { Location.distance(it.location, location) <= range.toDistance() }
        )
    }

    override suspend fun fetchRestaurantStreet(): RestaurantStreet {
        return RestaurantStreet(restaurantDao.getRestaurantList().orEmpty())
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        return restaurantDao.getRestaurantData(id)
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        restaurantDao.insertRestaurantData(restaurantData)
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        restaurantStreet.restaurantDataList.forEach { restaurantDao.insertRestaurantData(it) }
    }

    override fun deleteRestaurants() {
        restaurantDao.deleteRestaurantStreet()
    }

    override fun deleteRestaurantData(id: Id) {
        restaurantDao.deleteRestaurantData(id)
    }
}