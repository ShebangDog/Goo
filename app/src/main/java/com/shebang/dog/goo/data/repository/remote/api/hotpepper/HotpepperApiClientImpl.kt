package com.shebang.dog.goo.data.repository.remote.api.hotpepper

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.response.hotpepper.Shop
import retrofit2.HttpException

class HotpepperApiClientImpl(
    private val hotpepperApi: HotpepperApi,
    private val apiToken: String
) : HotpepperApiClient {

    override suspend fun fetchHotpepper(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int,
        dataCount: Int,
        format: Format
    ): RestaurantStreet = try {
        val hotpepperData = hotpepperApi.fetchHotpepper(
            apiToken,
            latitude.value,
            longitude.value,
            range.value,
            index,
            dataCount,
            Format.Json.value
        )

        val restaurantList: List<RestaurantData> =
            extractRestaurantDataList(hotpepperData.results?.shop)

        RestaurantStreet(restaurantList)
    } catch (httpException: HttpException) {
        RestaurantStreet(listOf())
    }


    override suspend fun fetchHotpepper(id: Id, format: Format): RestaurantData? = try {
        extractRestaurantDataList(
            hotpepperApi.fetchHotpepper(
                apiToken,
                id.value,
                Format.Json.value
            ).results?.shop
        ).first { it.id == id }
    } catch (httpException: HttpException) {
        null
    }

    private fun extractRestaurantDataList(shopList: List<Shop>?): List<RestaurantData> {
        return shopList
            ?.map {
                RestaurantData(
                    Id(it.id ?: ""),
                    it.name ?: "",
                    it.logoImage ?: "",
                    Location(
                        Latitude(it.lat?.toDouble() ?: 0.0),
                        Longitude(it.lng?.toDouble() ?: 0.0)
                    )
                )
            } ?: emptyList()
    }
}