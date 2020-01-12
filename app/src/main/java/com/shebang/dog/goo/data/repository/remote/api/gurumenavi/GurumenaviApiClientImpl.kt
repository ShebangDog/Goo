package com.shebang.dog.goo.data.repository.remote.api.gurumenavi

import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.response.gurumenavi.Rest
import retrofit2.HttpException

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
    ): RestaurantStreet = try {

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

        RestaurantStreet(restaurantDataList)
    } catch (httpException: HttpException) {
        RestaurantStreet(listOf())
    }

    override suspend fun fetchGurumenavi(id: Id): RestaurantData? = try {
        extractRestaurantDataList(
            gurumenaviApi.fetchGurumenavi(apiToken, id.value).rest
        ).first { it.id == id }
    } catch (httpException: HttpException) {
        null
    }

    private fun extractRestaurantDataList(restList: List<Rest>?): List<RestaurantData> {
        return restList
            ?.map {
                RestaurantData(
                    Id(it.id ?: ""),
                    Name(it.name ?: ""),
                    it.getImageUrlList().filterNotNull(),
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

    private fun Rest.getImageUrlList(): List<String?> {
        return listOf(imageUrl?.shopImage1, imageUrl?.shopImage2)
    }
}