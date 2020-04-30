package com.shebang.dog.goo.data.local

import com.shebang.dog.goo.data.RestaurantDataSource
import com.shebang.dog.goo.model.EmptyRestaurantStreet
import com.shebang.dog.goo.model.query.Index
import com.shebang.dog.goo.model.query.Range
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.model.restaurant.RestaurantData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantLocalDataSource @Inject constructor(private val restaurantDao: RestaurantDao) :
    RestaurantDataSource {

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int
    ): Flow<RestaurantStreet> {

        return flowOf(EmptyRestaurantStreet).flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(): Flow<RestaurantStreet> {
        return restaurantDao.getRestaurantList().map { RestaurantStreet(it) }.flowOn(Dispatchers.IO)
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        return withContext(Dispatchers.IO) { restaurantDao.getRestaurantData(id) }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        withContext(Dispatchers.IO) { restaurantDao.insertRestaurantData(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        withContext(Dispatchers.IO) {
            restaurantStreet.restaurantDataList.forEach {
                restaurantDao.insertRestaurantData(it)
            }
        }
    }

    override fun deleteRestaurants() {
        restaurantDao.deleteRestaurantStreet()
    }

    override fun deleteRestaurantData(id: Id) {
        restaurantDao.deleteRestaurantData(id)
    }
}