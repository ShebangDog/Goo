package com.shebang.dog.goo.data.repository.remote

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.remote.api.gurumenavi.GurumenaviApiClient
import com.shebang.dog.goo.data.repository.remote.api.hotpepper.HotpepperApiClient
import javax.inject.Inject

class RestaurantRemoteDataSource @Inject constructor(
    hotpepperApiClient: HotpepperApiClient,
    gurumenaviApiClient: GurumenaviApiClient
) : RestaurantDataSource {

    private val hotpepperStreet = HotpepperStreet(hotpepperApiClient)
    private val gurumenaviStreet = GurumenaviStreet(gurumenaviApiClient)
    private val streets = listOf(hotpepperStreet, gurumenaviStreet)

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): FindData<RestaurantStreet> {
        val hotpepperResult = hotpepperStreet.fetchRestaurantStreet(location, range)
        val gurumenaviResult = gurumenaviStreet.fetchRestaurantStreet(location, range)

        return hotpepperResult.merge(gurumenaviResult) { left, right ->
            RestaurantStreet(left.restaurantDataList + right.restaurantDataList)
        }
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        val hotpepperResult = hotpepperStreet.fetchRestaurant(id)
        val gurumenaviResult = gurumenaviStreet.fetchRestaurant(id)

        return hotpepperResult.elvis(gurumenaviResult).elvis(FindData.NotFound())
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        streets.forEach { it.saveRestaurant(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        streets.forEach { it.saveRestaurants(restaurantStreet) }
    }

    override fun deleteRestaurants() {
        streets.forEach { it.deleteRestaurants() }
    }

    override fun deleteRestaurantData(id: Id) {
        streets.forEach { it.deleteRestaurantData(id) }
    }

    private class HotpepperStreet(private val hotpepperApiClient: HotpepperApiClient) :
        RestaurantDataSource {

        override suspend fun fetchRestaurantStreet(
            location: Location,
            range: Range
        ): FindData<RestaurantStreet> {
            val hotpepperStreet =
                hotpepperApiClient.fetchHotpepper(location.latitude, location.longitude, range)


            return when (hotpepperStreet.restaurantDataList.isEmpty()) {
                true -> FindData.NotFound()
                else -> FindData.Found(hotpepperStreet)
            }
        }

        override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
            return FindData.Found(hotpepperApiClient.fetchHotpepper(id))
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

    private class GurumenaviStreet(private val gurumenaviApiClient: GurumenaviApiClient) :
        RestaurantDataSource {
        override suspend fun fetchRestaurantStreet(
            location: Location,
            range: Range
        ): FindData<RestaurantStreet> {

            val result = gurumenaviApiClient.fetchGurumenavi(
                location.latitude,
                location.longitude,
                range
            )

            return when (result.restaurantDataList.isEmpty()) {
                true -> FindData.NotFound()
                else -> FindData.Found(result)
            }

        }

        override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
            return FindData.Found(
                gurumenaviApiClient.fetchGurumenavi(id)
            )
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
}