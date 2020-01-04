package com.shebang.dog.goo.data

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.util.DebugHelper
import javax.inject.Inject

class FakeLocalDataSource @Inject constructor() : RestaurantDataSource {
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
        DebugHelper.log("cant save")
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        DebugHelper.log("cant save")
    }

    override fun deleteRestaurants() {
        DebugHelper.log("cant delete")
    }

    override fun deleteRestaurantData(id: Id) {
        DebugHelper.log("cant delete")
    }
}