package com.shebang.dog.goo.data.repository.remote.api.gurumenavi

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.response.gurumenavi.Rest

class GurumenaviApiClientImpl(
    private val gurumenaviApi: GurumenaviApi,
    private val apiToken: String
) : GurumenaviApiClient {
    override suspend fun fetchGurumenavi(
        latitude: Latitude,
        longitude: Longitude,
        range: Range,
        index: Int,
        dataCount: Int
    ): RestaurantStreet {

        val restaurantDataList = extractRestaurantDataList(
            gurumenaviApi.fetchGurumenavi(
                apiToken,
                latitude.value,
                longitude.value,
                range.value,
                index,
                dataCount
            ).rest
        )

        return RestaurantStreet(restaurantDataList)
    }

    override suspend fun fetchGurumenavi(id: Id): RestaurantData {
        val extractRestaurantDataList = extractRestaurantDataList(
            gurumenaviApi.fetchGurumenavi(apiToken, id.value).rest
        )

        return extractRestaurantDataList.first()
    }

    private fun extractRestaurantDataList(restList: List<Rest>?): List<RestaurantData> {
        return restList
            ?.map {
                RestaurantData(
                    Id(it.id ?: ""),
                    it.name ?: "",
                    it.getImageUrl() ?: "",
                    Location(
                        Latitude(
                            it.latitude?.let { value -> if (value == "") null else value }?.toDouble()
                                ?: 0.0
                        ),
                        Longitude(
                            it.longitude?.let { value -> if (value == "") null else value }?.toDouble()
                                ?: 0.0
                        )
                    )
                )
            } ?: emptyList()
    }

    private fun Rest.getImageUrl(): String? {
        return imageUrl?.shopImage1 ?: imageUrl?.shopImage2
    }
}