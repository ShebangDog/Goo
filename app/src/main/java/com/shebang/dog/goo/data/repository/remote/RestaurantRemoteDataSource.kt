package com.shebang.dog.goo.data.repository.remote

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApiClient

class RestaurantRemoteDataSource(private val apiClient: HotpepperApiClient) : RestaurantDataSource {
    override suspend fun fetchRestaurantStreet(
        latitude: Latitude,
        longitude: Longitude,
        range: Range
    ): FindData<RestaurantStreet> {

        val restaurantDataList = apiClient.fetchHotpepper(
            Latitude(35.669220),
            Longitude(139.761457),
            Range(1)
        ).restaurantDataList

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