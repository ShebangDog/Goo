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

    private val streets = listOf(
        GurumenaviStreet(gurumenaviApiClient)
    )

    override suspend fun fetchRestaurantStreet(): RestaurantStreet {
        return EmptyRestaurantStreet
    }

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Int,
        dataCount: Int
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

            return list
        }

        return RestaurantStreet(
            streets.fold<RestaurantDataSource, RestaurantStreet>(
                EmptyRestaurantStreet
            ) { result, dataSource ->
                result + dataSource.fetchRestaurantStreet(location, range, index, dataCount)
            }.restaurantDataList.distinctAndFuse()
        )
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        return streets.fold<RestaurantDataSource, RestaurantData?>(null) { result, dataSource ->
            result ?: dataSource.fetchRestaurant(id)
        }
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

        override suspend fun fetchRestaurantStreet(): RestaurantStreet {
            return EmptyRestaurantStreet
        }

        override suspend fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Int,
            dataCount: Int
        ): RestaurantStreet {
            return hotpepperApiClient.fetchHotpepper(
                location.latitude,
                location.longitude,
                range,
                index,
                dataCount
            )
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

        override suspend fun fetchRestaurantStreet(): RestaurantStreet {
            return EmptyRestaurantStreet
        }

        override suspend fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Int,
            dataCount: Int
        ): RestaurantStreet {

            return gurumenaviApiClient.fetchGurumenavi(
                location.latitude,
                location.longitude,
                range,
                index, dataCount
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