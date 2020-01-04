package com.shebang.dog.goo.fake

import com.shebang.dog.goo.data.model.*

object FakeReturnValue {
    private val restaurantListForTesting = listOf(
        RestaurantData(
            Id("ID_A"),
            "Name_A",
            "ImageURL_A",
            Location(
                Latitude(97.0),
                Longitude(97.0)
            )
        ),
        RestaurantData(
            Id("ID_B"),
            "Name_B",
            "ImageURL_B",
            Location(
                Latitude(98.0),
                Longitude(98.0)
            )
        ),
        RestaurantData(
            Id("ID_C"),
            "Name_C",
            "ImageURL_C",
            Location(
                Latitude(99.0),
                Longitude(99.0)
            )
        ),
        RestaurantData(
            Id("ID_D"),
            "Name_D",
            "ImageURL_D",
            Location(
                Latitude(100.0),
                Longitude(100.0)
            )
        )
    )

    val restaurantStreetForTesting = RestaurantStreet(restaurantListForTesting)

    val location = Location(
        Latitude(35.669220),
        Longitude(139.761457)
    )


}