package com.shebang.dog.goo.model

import java.net.URL

data class RestaurantData (
    val id: Id,
    val name: String,
    val imageUrl: URL,
    val location: Location
)