package com.shebang.dog.goo.data.repository.remote

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApiClient
import javax.inject.Inject

class RestaurantRemoteDataSource @Inject constructor(private val apiClient: HotpepperApiClient) :
    RestaurantDataSource {

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): FindData<RestaurantStreet> {

        val restaurantDataList =
            apiClient.fetchHotpepper(location.latitude, location.longitude, range)
                .restaurantDataList

        return when (restaurantDataList.isEmpty()) {
            true -> FindData.NotFound()
            else -> FindData.Found(RestaurantStreet(restaurantDataList))
        }
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        return FindData.Found(apiClient.fetchHotpepper(id))
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