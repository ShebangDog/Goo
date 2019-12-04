package com.shebang.dog.goo.repository

import com.shebang.dog.goo.model.FindData
import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

class RestaurantRepository(
    private val restaurantLocalDataSource: RestaurantDataSource
) : RestaurantDataSource {
    private var cache: RestaurantStreet? = null

    override suspend fun fetchRestaurantStreet(): FindData<RestaurantStreet> {
        if (cache?.restaurantDataList.isNullOrEmpty()) {
            cache = when (val result = restaurantLocalDataSource.fetchRestaurantStreet()) {
                is FindData.NotFound -> cache
                is FindData.Found -> result.value
            }
        }

        return when (cache?.restaurantDataList.isNullOrEmpty()) {
            true -> FindData.NotFound()
            else -> FindData.Found(cache!!)
        }
    }

    override suspend fun fetchRestaurant(id: Id): FindData<RestaurantData> {
        var restaurantData = cache?.restaurantDataList
            ?.asSequence()
            ?.filter { it.id == id }
            ?.first()

        if (restaurantData == null) {
            cache = RestaurantStreet(
                when (val result = restaurantLocalDataSource.fetchRestaurant(id)) {
                    is FindData.NotFound -> cache?.restaurantDataList.orEmpty()
                    is FindData.Found -> {
                        restaurantData = result.value
                        cache?.restaurantDataList.orEmpty() + result.value
                    }
                }
            )
        }

        return when (restaurantData) {
            null -> FindData.NotFound()
            else -> FindData.Found(restaurantData)
        }
    }

    override suspend fun saveRestaurant(restaurantData: RestaurantData) {
        restaurantLocalDataSource.saveRestaurant(restaurantData)
    }

    override suspend fun saveRestaurants(restaurantStreet: RestaurantStreet) {
        restaurantLocalDataSource.saveRestaurants(restaurantStreet)
    }

    override fun deleteRestaurants() {
        restaurantLocalDataSource.deleteRestaurants()
    }
}