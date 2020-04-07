package com.shebang.dog.goo.data.remote.api.hotpepper

import com.shebang.dog.goo.model.*
import com.shebang.dog.goo.data.response.hotpepper.Shop
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
        EmptyRestaurantStreet
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
        fun List<String>.isContainedIn(string: String): Boolean {
            return fold(false) { res, elem ->
                res || string.contains(elem)
            }
        }

        fun String.hasExtensionOfImage(): Boolean {
            return this.contains(Regex(pattern = ".*\\.(jpg|jpeg|png|gif|bmp)"))
        }

        val excludingImageList = listOf("noimage")

        return shopList
            ?.map {
                val location = if (it.lat != null && it.lng != null) Location(
                    Latitude(it.lat!!.toDouble()),
                    Longitude(it.lng!!.toDouble())
                ) else null

                RestaurantData(
                    Id(it.id ?: ""),
                    Name(it.name ?: ""),
                    ImageUrl(
                        it.getImageUrlList()
                            .filterNotNull()
                            .filter { url ->
                                url.isNotBlank() && !excludingImageList.isContainedIn(url) && url.hasExtensionOfImage()
                            }
                    ),
                    location,
                    Favorite(false)
                )
            } ?: emptyList()
    }

    private fun Shop.getImageUrlList(): List<String?> {
        return listOf(logoImage, urls?.pc, photo?.pc?.l)
    }
}