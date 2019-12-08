package com.shebang.dog.goo.data.repository

import com.shebang.dog.goo.data.model.*

class RestaurantRepository(
    private val restaurantLocalDataSource: RestaurantDataSource,
    private val restaurantRemoteDataSource: RestaurantDataSource
) : RestaurantDataSource {
    private var cache: RestaurantStreet? = null

    override suspend fun fetchRestaurantStreet(
        latitude: Latitude,
        longitude: Longitude,
        range: Range
    ): FindData<RestaurantStreet> {

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>?
        ): FindData<RestaurantStreet> {

            return when (restaurantDataSourceList.isNullOrEmpty()) {
                true -> FindData.NotFound()
                else -> {
                    val dataSource = restaurantDataSourceList.first()
                    when (val result =
                        dataSource.fetchRestaurantStreet(latitude, longitude, range)) {

                        is FindData.NotFound -> fetch(restaurantDataSourceList.drop(1))
                        is FindData.Found -> result
                    }
                }
            }
        }


        return when (cache?.restaurantDataList.isNullOrEmpty()) {
            true -> fetch(listOf(restaurantRemoteDataSource, restaurantLocalDataSource))
            else -> FindData.Found(cache!!)
        }
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        fun <T> List<T>.quickFilter(function: (T) -> Boolean): T {
            return this
                .asSequence()
                .filter(function)
                .first()
        }

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): FindData<RestaurantData> {
            return when (restaurantDataSourceList.isNullOrEmpty()) {
                true -> FindData.NotFound()
                else -> {
                    val dataSource = restaurantDataSourceList.first()

                    when (val result = dataSource.fetchRestaurant(id)) {
                        is FindData.NotFound -> fetch(restaurantDataSourceList.drop(1))
                        is FindData.Found -> FindData.Found(result.value)
                    }
                }
            }
        }

        return when (val restaurantData = cache?.restaurantDataList?.quickFilter { it.id == id }) {
            null -> fetch(listOf(restaurantRemoteDataSource, restaurantLocalDataSource))
            else -> FindData.Found(restaurantData)
        }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        restaurantLocalDataSource.saveRestaurant(restaurantData)
        restaurantRemoteDataSource.saveRestaurant(restaurantData)
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        restaurantLocalDataSource.saveRestaurants(restaurantStreet)
        restaurantRemoteDataSource.saveRestaurants(restaurantStreet)
    }

    override fun deleteRestaurants() {
        restaurantLocalDataSource.deleteRestaurants()
        restaurantRemoteDataSource.deleteRestaurants()
    }

    override fun deleteRestaurantData(id: Id) {
        restaurantLocalDataSource.deleteRestaurantData(id)
        restaurantRemoteDataSource.deleteRestaurantData(id)
    }
}