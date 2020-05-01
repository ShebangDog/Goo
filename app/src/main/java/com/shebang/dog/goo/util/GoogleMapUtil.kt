package com.shebang.dog.goo.util

import com.shebang.dog.goo.model.location.Location

object GoogleMapUtil {

    private const val BaseUrl = "http://maps.google.com/maps"
    const val PackageName = "com.google.android.apps.maps"

    fun createUri(userLocation: Location, restaurantLocation: Location): String {
        val userLocationForQuery = locationAsQuery(userLocation)
        val restaurantLocationQuery = locationAsQuery(restaurantLocation)

        return createUri(userLocationForQuery, restaurantLocationQuery)
    }

    fun createUri(userLocation: Location, restaurantName: String): String {
        val locationString = locationAsQuery(userLocation)

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

    private fun locationAsQuery(location: Location): String {
        return listOf(location.latitude.value, location.longitude.value).joinToString(",")
    }
}