package com.shebang.dog.goo.model

data class Location(val latitude: Latitude, val longitude: Longitude) {
    companion object {
        fun distance(first: Location, second: Location): Distance {
            val locationList = mapOf("first" to first, "second" to second).map {
                android.location.Location(it.key).apply {
                    this.latitude = it.value.latitude.value
                    this.longitude = it.value.longitude.value
                }
            }

            return Distance(locationList.first().distanceTo(locationList[1]).toDouble())
        }
    }
}