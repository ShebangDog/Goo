package com.shebang.dog.goo.data.remote.api.gurumenavi

import com.shebang.dog.goo.data.model.location.Latitude
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.location.Longitude
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.*
import com.shebang.dog.goo.data.response.gurumenavi.Rest
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
    ): List<RestaurantData> = try {

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

        restaurantDataList
    } catch (httpException: HttpException) {
        emptyList()
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
                val rawLatitude = it.latitude?.takeIf { str -> str.isNotBlank() }?.toDouble()
                val rawLongitude = it.longitude?.takeIf { str -> str.isNotBlank() }?.toDouble()

                val location = if (rawLatitude != null && rawLongitude != null)
                    Location(
                        Latitude(rawLatitude),
                        Longitude(rawLongitude)
                    ) else null

                val address = it.address ?: ""
                val phoneNumber = it.tel ?: ""

                RestaurantData(
                    Id(it.id ?: ""),
                    Name(it.name ?: ""),
                    ImageUrl(
                        it.getImageUrlList().filterNotNull().filter { url -> url.isNotBlank() }),
                    PhoneNumber(phoneNumber),
                    Address(address),
                    location,
                    Favorite(false)
                )
            } ?: emptyList()
    }

    private fun Rest.getImageUrlList(): List<String?> {
        return listOf(imageUrl?.shopImage1, imageUrl?.shopImage2)
    }
}