package com.shebang.dog.goo.data

import com.shebang.dog.goo.model.*
import com.shebang.dog.goo.di.annotation.scope.LocalDataSource
import com.shebang.dog.goo.di.annotation.scope.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    @LocalDataSource private val restaurantLocalDataSource: RestaurantDataSource,
    @RemoteDataSource private val restaurantRemoteDataSource: RestaurantDataSource
) : RestaurantDataSource {
    private var cache: RestaurantStreet? = null
    private val dataSourceList = listOf(restaurantRemoteDataSource, restaurantLocalDataSource)

    override fun fetchRestaurantStreet(): Flow<RestaurantStreet> {
        return restaurantLocalDataSource.fetchRestaurantStreet()
    }

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int
    ): Flow<RestaurantStreet> {

        fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): Flow<RestaurantStreet> {

            return when (restaurantDataSourceList.isEmpty()) {
                true -> flowOf(EmptyRestaurantStreet).flowOn(Dispatchers.IO)
                false -> {
                    val dataSource = restaurantDataSourceList.first()
                    val result = dataSource.fetchRestaurantStreet(location, range, index, dataCount)

                    result.flatMapLatest {
                        if (it.restaurantDataList.isEmpty()) fetch(restaurantDataSourceList.drop(1))
                        else flowOf(it).flowOn(Dispatchers.IO)
                    }
                }
            }
        }

        fun Flow<RestaurantStreet>.applyFavorite(): Flow<RestaurantStreet> {
            return map { restaurantStreet ->
                RestaurantStreet(restaurantStreet.restaurantDataList.map {
                    restaurantLocalDataSource.fetchRestaurant(
                        it.id
                    ) ?: it
                })
            }
        }

        return fetch(dataSourceList).applyFavorite().onEach {
            updateCache(it)
            saveRestaurants(it)
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