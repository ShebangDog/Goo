package com.shebang.dog.goo.data

import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import kotlinx.coroutines.flow.Flow

interface RestaurantDataSource {
    fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int = 5
    ): Flow<List<RestaurantData>>

    fun fetchRestaurantStreet(): Flow<List<RestaurantData>>

    suspend fun fetchRestaurant(id: Id): RestaurantData?

    suspend fun saveRestaurant(restaurantData: RestaurantData)

    suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>)

    fun deleteRestaurants()

    fun deleteRestaurantData(id: Id)
}