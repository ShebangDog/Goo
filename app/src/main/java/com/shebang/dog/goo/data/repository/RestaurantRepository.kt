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
    ): FindData<RestaurantStreet> {

        suspend fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): FindData<RestaurantStreet> {

            return when (restaurantDataSourceList.isEmpty()) {
                true -> FindData.NotFound()
                else -> {
                    val dataSource = restaurantDataSourceList.first()
                    when (val result =
                        dataSource.fetchRestaurantStreet(location, range)) {

                        is FindData.Found -> result
                        is FindData.NotFound -> fetch(restaurantDataSourceList.drop(1))
                    }
                }
            }
        }

        return when (cache?.restaurantDataList.isNullOrEmpty()) {
            true -> fetch(dataSourceList).also { updateCache(it) }
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
            return when (restaurantDataSourceList.isEmpty()) {
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
            null -> fetch(dataSourceList).also { findData ->
                findData.ifFound {
                    updateCache(
                        FindData.Found(
                            RestaurantStreet(listOf(it))
                        )
                    )
                }
            }

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

    private fun updateCache(findData: FindData<RestaurantStreet>) {
        val street = RestaurantStreet(cache?.restaurantDataList ?: emptyList())
        cache = findData.map {
            RestaurantStreet(
                (it.restaurantDataList + street.restaurantDataList).distinctBy { elem -> elem.id.value }
            )
        }.orElse(street)
    }
}