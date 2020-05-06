package com.shebang.dog.goo.data

import com.shebang.dog.goo.model.location.Latitude
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.location.Longitude
import com.shebang.dog.goo.model.restaurant.*
import org.junit.Before

class RestaurantDataTest {
    private lateinit var restaurantData: RestaurantData

    @Before
    fun setUp() {
        restaurantData = RestaurantData(
            id = Id("restaurant-id"),
            name = Name("restaurant-name"),
            imageUrl = ImageUrl(listOf("https://avatars3.githubusercontent.com/u/38370581?s=400&u=2f167ff1f76ad0097d4097ed48041afbaca44bfc&v=4")),
            address = Address("〒100-8111 東京都千代田区千代田１−１"),
            favorite = Favorite(false),
            location = Location(latitude = Latitude(35.678803), longitude = Longitude(139.756263)),
            phoneNumber = PhoneNumber("123-4567-8901")
        )
    }
}