package com.shebang.dog.goo.repository.local

import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.repository.RestaurantDataSource

class RestaurantLocalDataSource(private val restaurantDatabase: RestaurantDatabase) :
    RestaurantDataSource {
    private val restaurantDao = restaurantDatabase.restaurantDao()

    override suspend fun loadRestaurants(callback: RestaurantDataSource.LoadRestaurantCallback) {
        restaurantDao.getRestaurantStreet()?.also {
            if (!it.isNullOrEmpty()) callback.onLoad(RestaurantStreet(it))
            else callback.onFail()
        }
    }

    override suspend fun loadRestaurant(
        id: Id,
        callback: RestaurantDataSource.GetRestaurantCallback
    ) {
        restaurantDao.getRestaurantData(id).also {
            if (it != null) callback.onGet(it)
            else callback.onFail()
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
}