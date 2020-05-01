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

        return createUri(userLocationForQuery, restaurantLocationQuery)
    }

    fun createUri(userLocation: Location, restaurantName: String): String {
        val locationString = userLocation.let { "${it.latitude.value},${it.longitude.value}" }

        return createUri(locationString, restaurantName)
    }

    fun createUri(currentLocationName: String, restaurantName: String): String {
        val queryItemList = QueryItems.values()
        val queryValueList = listOf(
            currentLocationName,
            restaurantName,
            DirFlags.Walk.value
        )

        val question = "?"
        val queryList = queryItemList.zip(queryValueList).map { pair ->
            val queryItem = pair.first
            val queryValue = pair.second
            val equal = "="

            queryItem.value + equal + queryValue
        }.joinToString("&")

        return (BaseUrl + question + queryList).also { DebugHelper.log(it) }
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