package com.shebang.dog.goo.data.repository

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.di.annotation.scope.LocalDataSource
import com.shebang.dog.goo.di.annotation.scope.RemoteDataSource
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    @LocalDataSource private val restaurantLocalDataSource: RestaurantDataSource,
    @RemoteDataSource private val restaurantRemoteDataSource: RestaurantDataSource
) : RestaurantDataSource {
    private var cache: RestaurantStreet? = null
    private val dataSourceList = listOf(restaurantRemoteDataSource, restaurantLocalDataSource)

    override suspend fun fetchRestaurantStreet(): RestaurantStreet {
        return restaurantLocalDataSource.fetchRestaurantStreet()
    }

    override suspend fun fetchRestaurantStreet(
        location: Location,
        range: Range
    ): RestaurantStreet {

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): RestaurantStreet {

            return when (restaurantDataSourceList.isEmpty()) {
                true -> EmptyRestaurantStreet
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

        suspend fun RestaurantStreet.applyFavorite(): RestaurantStreet {
            return RestaurantStreet(
                restaurantDataList.map { restaurantLocalDataSource.fetchRestaurant(it.id) ?: it }
            )
        }

        return when (cache?.restaurantDataList.isNullOrEmpty()) {
            true -> fetch(dataSourceList).applyFavorite()
                .also {
                    updateCache(it)
                    saveRestaurants(it)
                }
            else -> cache!!
        }
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        fun <T> List<T>.quickPick(function: (T) -> Boolean): T? {
            return this
                .asSequence()
                .filter(function)
                .firstOrNull()
        }

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): RestaurantData? {
            return restaurantDataSourceList
                .takeIf { it.isNotEmpty() }
                ?.let {
                    val dataSource = restaurantDataSourceList.first()

                    dataSource.fetchRestaurant(id)
                } ?: fetch(restaurantDataSourceList.drop(1))
        }

        val restaurantData = cache?.restaurantDataList?.quickPick { it.id == id }

        return restaurantData ?: fetch(dataSourceList)
            ?.also {
                updateCache(RestaurantStreet(listOf(it)))
                saveRestaurant(it)
            }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        dataSourceList.forEach { it.saveRestaurant(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        dataSourceList.forEach { it.saveRestaurants(restaurantStreet) }
    }

    override fun deleteRestaurants() {
        dataSourceList.forEach { it.deleteRestaurants() }
    }

    override fun deleteRestaurantData(id: Id) {
        dataSourceList.forEach { it.deleteRestaurantData(id) }
    }

    private fun updateCache(restaurantStreet: RestaurantStreet) {
        val street = RestaurantStreet(cache?.restaurantDataList ?: emptyList())
        val fusedStreet = (restaurantStreet + street).restaurantDataList

        cache = when (fusedStreet.isEmpty()) {
            true -> cache
            false -> RestaurantStreet(fusedStreet.distinctBy { it.id })
        }
    }
}