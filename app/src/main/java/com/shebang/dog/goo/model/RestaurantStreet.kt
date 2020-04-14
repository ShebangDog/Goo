package com.shebang.dog.goo.model

import com.shebang.dog.goo.model.restaurant.RestaurantData

open class RestaurantStreet(val restaurantDataList: List<RestaurantData>) {
    infix operator fun plus(other: RestaurantStreet): RestaurantStreet {
        return RestaurantStreet(this.restaurantDataList + other.restaurantDataList)
    }
}

object EmptyRestaurantStreet : RestaurantStreet(emptyList())