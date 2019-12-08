package com.shebang.dog.goo.data.repository.local

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource

class RestaurantLocalDataSource(restaurantDatabase: RestaurantDatabase) :
    RestaurantDataSource {
    private val restaurantDao = restaurantDatabase.restaurantDao()

    override suspend fun fetchRestaurantStreet(
        latitude: Latitude,
        longitude: Longitude,
        range: Range
    ): FindData<RestaurantStreet> {

        return when (val restaurantList = restaurantDao.getRestaurantList()) {
            null -> FindData.NotFound()
            else -> FindData.Found(RestaurantStreet(restaurantList))
        }
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        return when (val restaurantData = restaurantDao.getRestaurantData(id)) {
            null -> FindData.NotFound()
            else -> FindData.Found(restaurantData)
        }
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

    override fun deleteRestaurantData(id: Id) {
        restaurantDao.deleteRestaurantData(id)
    }
}