package com.shebang.dog.goo.data.repository.remote

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.remote.api.gurumenavi.GurumenaviApiClient
import com.shebang.dog.goo.data.repository.remote.api.hotpepper.HotpepperApiClient
import com.shebang.dog.goo.util.DebugHelper
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
    ): RestaurantStreet {
        fun List<RestaurantData>.distinctAndFuse(): List<RestaurantData> {
            val set = HashSet<String>()
            val list = ArrayList<RestaurantData>()
            for (elem in this) {
                val key = elem.name.formalize()

                list.add(
                    if (set.add(key)) elem
                    else {
                        val index = list
                            .mapIndexed { index, restaurant -> index to restaurant }
                            .filter { it.second.name.formalize() == key }
                            .map { it.first }
                            .first()

                        list[index]
                            .also { list.remove(it) }
                            .let { it + elem }
                    }
                )
            }

            return list.also { DebugHelper.log(it) }
        }

        val hotpepperResult =
            hotpepperStreet.fetchRestaurantStreet(location, range)

        val gurumenaviResult =
            gurumenaviStreet.fetchRestaurantStreet(location, range)

        return RestaurantStreet(
            (hotpepperResult.restaurantDataList + gurumenaviResult.restaurantDataList)
                .distinctAndFuse()
        )
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        val hotpepperResult = hotpepperStreet.fetchRestaurant(id)
        val gurumenaviResult = gurumenaviStreet.fetchRestaurant(id)

        return hotpepperResult ?: gurumenaviResult
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
        ): RestaurantStreet {
            return hotpepperApiClient.fetchHotpepper(location.latitude, location.longitude, range)
        }

        override suspend fun fetchRestaurant(id: Id): RestaurantData? {
            return hotpepperApiClient.fetchHotpepper(id)
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
        ): RestaurantStreet {

            return gurumenaviApiClient.fetchGurumenavi(
                location.latitude,
                location.longitude,
                range
            )
        }

        override suspend fun fetchRestaurant(id: Id): RestaurantData? {
            return gurumenaviApiClient.fetchGurumenavi(id)
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