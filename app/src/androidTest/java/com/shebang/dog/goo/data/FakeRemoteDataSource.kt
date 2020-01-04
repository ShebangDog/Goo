package com.shebang.dog.goo.data

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor() : RestaurantDataSource {
    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): FindData<RestaurantStreet> {
        return FindData.NotFound()
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        return FindData.NotFound()
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {

    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {

    }

    override fun deleteRestaurants() {

    }

    override fun deleteRestaurantData(id: Id) {

    }
}