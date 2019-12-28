package com.shebang.dog.goo.data.repository.local

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import javax.inject.Inject

class RestaurantLocalDataSource @Inject constructor(private val restaurantDao: RestaurantDao) :
    RestaurantDataSource {

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): FindData<RestaurantStreet> {

        return when (val restaurantList = restaurantDao.getRestaurantList().orEmpty()) {
            emptyList<RestaurantData>() -> FindData.NotFound()
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