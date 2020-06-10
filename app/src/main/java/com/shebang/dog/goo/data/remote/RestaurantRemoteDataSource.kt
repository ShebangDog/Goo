package com.shebang.dog.goo.data.remote

import com.shebang.dog.goo.data.RestaurantDataSource
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import com.shebang.dog.goo.data.remote.api.gurumenavi.GurumenaviApiClient
import com.shebang.dog.goo.data.remote.api.hotpepper.HotpepperApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRemoteDataSource @Inject constructor(
    hotpepperApiClient: HotpepperApiClient,
    gurumenaviApiClient: GurumenaviApiClient
) : RestaurantDataSource {

    private val streets = listOf(
        GurumenaviStreet(gurumenaviApiClient), HotpepperStreet(hotpepperApiClient)
    )

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(): Flow<List<RestaurantData>> {
        fun fetch(
            dataSourceList: List<RestaurantDataSource>,
            restaurantStreet: List<RestaurantData> = emptyList()
        ): Flow<List<RestaurantData>> {

            return when (dataSourceList.isEmpty()) {
                true -> flowOf(restaurantStreet).flowOn(Dispatchers.IO)
                false -> {
                    val dataSource = dataSourceList.first()
                    dataSource.fetchRestaurantStreet().flatMapLatest {
                        fetch(dataSourceList.drop(1), restaurantStreet + it)
                    }
                }
            }
        }

        return fetch(streets)
    }

    @ExperimentalCoroutinesApi
    override fun fetchRestaurantStreet(
        location: Location,
        range: Range,
        index: Index,
        dataCount: Int
    ): Flow<List<RestaurantData>> {
        fun List<RestaurantData>.distinctAndFuse(): List<RestaurantData> {
            val set = HashSet<String>()
            val list = ArrayList<RestaurantData>()
            for (elem in this) {
                val key = elem.name.formalize()

                list.add(
                    if (set.add(key)) elem
                    else {
                        val index = list
                            .asSequence()
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

        val gurumenaviStreet =
            streets.first().fetchRestaurantStreet(location, range, index, dataCount)
        val hotpepperStreet = streets[1].fetchRestaurantStreet(location, range, index, dataCount)

        return combine(
            gurumenaviStreet,
            hotpepperStreet
        ) { gurumenavi, hotpepper -> (gurumenavi + hotpepper).distinctAndFuse() }
    }

    override suspend fun fetchRestaurant(id: Id): RestaurantData? {
        return streets.fold<RestaurantDataSource, RestaurantData?>(null) { result, dataSource ->
            result ?: dataSource.fetchRestaurant(id)
        }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        streets.forEach { it.saveRestaurant(restaurantData) }
    }

    override suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>) {
        streets.forEach { it.saveRestaurants(restaurantDataList) }
    }

    override fun deleteRestaurants() {
        streets.forEach { it.deleteRestaurants() }
    }

    override fun deleteRestaurantData(id: Id) {
        streets.forEach { it.deleteRestaurantData(id) }
    }

    private class HotpepperStreet(private val hotpepperApiClient: HotpepperApiClient) :
        RestaurantDataSource {

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(): Flow<List<RestaurantData>> {
            return flowOf(emptyList<RestaurantData>()).flowOn(Dispatchers.IO)
        }

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Index,
            dataCount: Int
        ): Flow<List<RestaurantData>> {
            return flow {
                emit(
                    hotpepperApiClient.fetchHotpepper(
                        location.latitude,
                        location.longitude,
                        range,
                        index.toHotpepperValue(dataCount),
                        dataCount
                    )
                )
            }.flowOn(Dispatchers.IO)
        }

        override suspend fun fetchRestaurant(id: Id): RestaurantData? {
            return withContext(Dispatchers.IO) { hotpepperApiClient.fetchHotpepper(id) }
        }

        override suspend fun saveRestaurant(restaurantData: RestaurantData) {

        }

        override suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>) {

        }

        override fun deleteRestaurants() {

        }

        override fun deleteRestaurantData(id: Id) {

        }
    }

    private class GurumenaviStreet(private val gurumenaviApiClient: GurumenaviApiClient) :
        RestaurantDataSource {

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(): Flow<List<RestaurantData>> {
            return flowOf(emptyList<RestaurantData>()).flowOn(Dispatchers.IO)
        }

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Index,
            dataCount: Int
        ): Flow<List<RestaurantData>> {

            return flow {
                emit(
                    gurumenaviApiClient.fetchGurumenavi(
                        location.latitude,
                        location.longitude,
                        range,
                        index.toGurumenaviValue(),
                        dataCount
                    )
                )
            }.flowOn(Dispatchers.IO)
        }

        override suspend fun fetchRestaurant(id: Id): RestaurantData? {
            return withContext(Dispatchers.IO) { gurumenaviApiClient.fetchGurumenavi(id) }
        }

        override suspend fun saveRestaurant(restaurantData: RestaurantData) {

        }

        override suspend fun saveRestaurants(restaurantDataList: List<RestaurantData>) {

        }

        override fun deleteRestaurants() {

        }

        override fun deleteRestaurantData(id: Id) {

        }
    }
}