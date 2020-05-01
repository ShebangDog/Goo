package com.shebang.dog.goo.util

import com.shebang.dog.goo.model.location.Location

object GoogleMapUtil {

    private const val BaseUrl = "http://maps.google.com/maps"
    const val PackageName = "com.google.android.apps.maps"

    fun createUri(userLocation: Location, restaurantLocation: Location): String {
        val userLocationForQuery = listOf(
            userLocation.latitude.value,
            userLocation.longitude.value
        ).joinToString(",")

        val restaurantLocationQuery = listOf(
            restaurantLocation.latitude.value,
            restaurantLocation.longitude.value
        ).joinToString(",")

        val queryItemList = QueryItems.values()
        val queryValueList = listOf(
            userLocationForQuery,
            restaurantLocationQuery,
            DirFlags.Walk.value
        )

        val question = "?"

        val queryList = queryItemList.zip(queryValueList).map { pair ->
            val queryItem = pair.first
            val queryValue = pair.second
            val equal = "="

            queryItem.value + equal + queryValue
        }.joinToString("&")

        return BaseUrl + question + queryList
    }


    private enum class QueryItems(val value: String) {
        StartAddress("saddr"),
        DestinationAddress("daddr"),
        DirectoryFlag("dirflg")
    }

    private enum class DirFlags(val value: String) {
        Ride("r"),
        Drive("d"),
        Walk("w")
    }
}