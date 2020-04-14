package com.shebang.dog.goo.model.location

import androidx.room.ColumnInfo
import com.shebang.dog.goo.model.restaurant.Distance

data class Location(
    @ColumnInfo(name = "latitude")
    val latitude: Latitude,

    @ColumnInfo(name = "longitude")
    val longitude: Longitude
) {
    override fun toString(): String {
        return "${latitude.value}$delimiter${longitude.value}"
    }

    companion object {
        const val delimiter = ":"

        fun distance(first: Location, second: Location): Distance {
            val locationList = mapOf("first" to first, "second" to second).map {
                android.location.Location(it.key).apply {
                    this.latitude = it.value.latitude.value
                    this.longitude = it.value.longitude.value
                }
            }

            return Distance(
                locationList.first().distanceTo(locationList[1]).toDouble()
            )
        }
    }
}