package com.shebang.dog.goo.data.local

import com.shebang.dog.goo.data.RestaurantDataSource
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
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
    ): Flow<List<RestaurantData>> {

        return flowOf(emptyList<RestaurantData>()).flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(): Flow<List<RestaurantData>> {
        return restaurantDao.getRestaurantList()
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        return withContext(Dispatchers.IO) { restaurantDao.getRestaurantData(id) }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        withContext(Dispatchers.IO) { restaurantDao.insertRestaurantData(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>) {
        withContext(Dispatchers.IO) {
            restaurantDataList.forEach { restaurantDao.insertRestaurantData(it) }
        }
    }

    override fun deleteRestaurants() {
        restaurantDao.deleteRestaurantStreet()
    }

    override fun deleteRestaurantData(id: Id) {
        restaurantDao.deleteRestaurantData(id)
    }
}