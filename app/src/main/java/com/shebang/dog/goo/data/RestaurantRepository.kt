package com.shebang.dog.goo.data

import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
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

    private var cache: List<RestaurantData> = emptyList()
    private val dataSourceList = listOf(restaurantLocalDataSource, restaurantRemoteDataSource)

    override fun fetchRestaurantStreet(): Flow<List<RestaurantData>> {
        return restaurantLocalDataSource.fetchRestaurantStreet()
    }

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int
    ): Flow<List<RestaurantData>> {

        fun fetch(
            restaurantDataSourceList: List<RestaurantDataSource>
        ): Flow<List<RestaurantData>> {

            return when (restaurantDataSourceList.isEmpty()) {
                true -> flowOf(emptyList<RestaurantData>()).flowOn(Dispatchers.IO)
                false -> {
                    val dataSource = restaurantDataSourceList.first()
                    val result = dataSource.fetchRestaurantStreet(location, range, index, dataCount)

                    result.flatMapLatest {
                        if (it.isEmpty()) fetch(restaurantDataSourceList.drop(1))
                        else flowOf(it).flowOn(Dispatchers.IO)
                    }
                }
            }
        }

        fun Flow<List<RestaurantData>>.applyFavorite(): Flow<List<RestaurantData>> {
            return map { restaurantList ->
                restaurantList
                    .map { restaurantLocalDataSource.fetchRestaurant(it.id) ?: it }
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

        val restaurantData = cache.quickPick { it.id == id }

        return restaurantData ?: (fetch(dataSourceList))?.also {
            saveRestaurant(it)
            updateCache(it)
        }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        dataSourceList.forEach { it.saveRestaurant(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>) {
        dataSourceList.forEach { it.saveRestaurants(restaurantDataList) }
    }

    override fun deleteRestaurants() {
        dataSourceList.forEach { it.deleteRestaurants() }
    }

    override fun deleteRestaurantData(id: Id) {
        dataSourceList.forEach { it.deleteRestaurantData(id) }
    }

    private fun updateCache(restaurantDataList: List<RestaurantData>) {
        val fusedList = restaurantDataList + cache

        cache = when (restaurantDataList.isEmpty()) {
            true -> cache
            false -> fusedList.distinctBy { it.id }
        }
    }

    private fun updateCache(restaurantData: RestaurantData) {
        val oldData = cache.quickPick { it.id == restaurantData.id }
        val newData = if (oldData == null) restaurantData else oldData + restaurantData

        val fusedList = (cache + newData).distinctBy { it.id }

        cache = fusedList
    }

    private fun <T> List<T>.quickPick(function: (T) -> Boolean): T? {
        return this
            .asSequence()
            .filter(function)
            .firstOrNull()
    }
}