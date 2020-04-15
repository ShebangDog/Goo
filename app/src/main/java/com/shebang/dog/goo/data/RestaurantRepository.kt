package com.shebang.dog.goo.data

import com.shebang.dog.goo.di.annotation.scope.LocalDataSource
import com.shebang.dog.goo.di.annotation.scope.RemoteDataSource
import com.shebang.dog.goo.model.EmptyRestaurantStreet
import com.shebang.dog.goo.model.Index
import com.shebang.dog.goo.model.Range
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.model.restaurant.RestaurantData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    @LocalDataSource private val restaurantLocalDataSource: RestaurantDataSource,
    @RemoteDataSource private val restaurantRemoteDataSource: RestaurantDataSource
) : RestaurantDataSource {

    private var cache: RestaurantStreet = EmptyRestaurantStreet
    private val dataSourceList = listOf(restaurantLocalDataSource, restaurantRemoteDataSource)

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
            saveRestaurants(it)
            updateCache(it)
        }
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
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

        val restaurantData = cache.restaurantDataList.quickPick { it.id == id }

        return restaurantData ?: (fetch(dataSourceList))?.also {
            saveRestaurant(it)
            updateCache(it)
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
        val fusedStreet = restaurantStreet + cache

        cache = when (fusedStreet.restaurantDataList.isEmpty()) {
            true -> cache
            false -> RestaurantStreet(fusedStreet.restaurantDataList.distinctBy { it.id })
        }
    }

    private fun updateCache(restaurantData: RestaurantData) {
        val oldData = cache.restaurantDataList.quickPick { it.id == restaurantData.id }
        val newData = if (oldData == null) restaurantData else oldData + restaurantData

        val fusedStreet =
            RestaurantStreet((cache.restaurantDataList + newData).distinctBy { it.id })

        cache = fusedStreet
    }

    private fun <T> List<T>.quickPick(function: (T) -> Boolean): T? {
        return this
            .asSequence()
            .filter(function)
            .firstOrNull()
    }
}