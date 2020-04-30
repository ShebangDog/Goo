package com.shebang.dog.goo.data.remote

import com.shebang.dog.goo.data.RestaurantDataSource
import com.shebang.dog.goo.data.remote.api.gurumenavi.GurumenaviApiClient
import com.shebang.dog.goo.data.remote.api.hotpepper.HotpepperApiClient
import com.shebang.dog.goo.model.EmptyRestaurantStreet
import com.shebang.dog.goo.model.query.Index
import com.shebang.dog.goo.model.query.Range
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.model.restaurant.RestaurantData
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
    override fun fetchRestaurantStreet(): Flow<RestaurantStreet> {
        fun fetch(
            dataSourceList: List<RestaurantDataSource>,
            restaurantStreet: RestaurantStreet = EmptyRestaurantStreet
        ): Flow<RestaurantStreet> {

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
    ): Flow<RestaurantStreet> {
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

        return combine(gurumenaviStreet, hotpepperStreet) { gurumenavi, hotpepper ->
            RestaurantStreet((gurumenavi + hotpepper).restaurantDataList.distinctAndFuse())
        }
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

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(): Flow<RestaurantStreet> {
            return flowOf(EmptyRestaurantStreet).flowOn(Dispatchers.IO)
        }

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Index,
            dataCount: Int
        ): Flow<RestaurantStreet> {
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

        override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {

        }

        override fun deleteRestaurants() {

        }

        override fun deleteRestaurantData(id: Id) {

        }
    }

    private class GurumenaviStreet(private val gurumenaviApiClient: GurumenaviApiClient) :
        RestaurantDataSource {

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(): Flow<RestaurantStreet> {
            return flowOf(EmptyRestaurantStreet).flowOn(Dispatchers.IO)
        }

        @ExperimentalCoroutinesApi
        override fun fetchRestaurantStreet(
            location: Location,
            range: Range,
            index: Index,
            dataCount: Int
        ): Flow<RestaurantStreet> {

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

        override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {

        }

        override fun deleteRestaurants() {

        }

        override fun deleteRestaurantData(id: Id) {

        }
    }
}