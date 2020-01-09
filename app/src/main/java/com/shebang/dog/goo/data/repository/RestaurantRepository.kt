package com.shebang.dog.goo.data.repository

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.di.scope.LocalDataSource
import com.shebang.dog.goo.di.scope.RemoteDataSource
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    @LocalDataSource private val restaurantLocalDataSource: RestaurantDataSource,
    @RemoteDataSource private val restaurantRemoteDataSource: RestaurantDataSource
) : RestaurantDataSource {
    private var cache: RestaurantStreet? = null
    private val dataSourceList = listOf(restaurantRemoteDataSource, restaurantLocalDataSource)

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): RestaurantStreet {

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): RestaurantStreet {

            return when (restaurantDataSourceList.isEmpty()) {
                true -> RestaurantStreet(listOf())
                else -> {
                    val dataSource = restaurantDataSourceList.first()
                    val result = dataSource.fetchRestaurantStreet(location, range)
                    when (result.restaurantDataList.isEmpty()) {
                        true -> fetch(restaurantDataSourceList.drop(1))
                        false -> result
                    }
                }
            }
        }

        return when (cache?.restaurantDataList.isNullOrEmpty()) {
            true -> fetch(dataSourceList).also { updateCache(it) }
            else -> cache!!
        }
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        fun <T> List<T>.quickFilter(function: (T) -> Boolean): T {
            return this
                .asSequence()
                .filter(function)
                .first()
        }

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): RestaurantData? {
            return when (restaurantDataSourceList.isEmpty()) {
                true -> null
                else -> {
                    val dataSource = restaurantDataSourceList.first()

                    when (val result = dataSource.fetchRestaurant(id)) {
                        null -> fetch(restaurantDataSourceList.drop(1))
                        else -> result
                    }
                }
            }
        }

        return when (val restaurantData = cache?.restaurantDataList?.quickFilter { it.id == id }) {
            null -> fetch(dataSourceList)?.also { updateCache(RestaurantStreet(listOf(it))) }
            else -> restaurantData
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

    private fun updateCache(restaurantStreet: RestaurantStreet) {
        val street = RestaurantStreet(cache?.restaurantDataList ?: emptyList())
        val fusedStreet = restaurantStreet.restaurantDataList + street.restaurantDataList

        cache = when (fusedStreet.isEmpty()) {
            true -> cache
            false -> RestaurantStreet(fusedStreet.distinctBy { it.id.value })
        }
    }
}