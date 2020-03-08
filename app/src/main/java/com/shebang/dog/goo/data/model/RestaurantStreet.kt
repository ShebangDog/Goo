package com.shebang.dog.goo.data.model

open class RestaurantStreet(val restaurantDataList: List<RestaurantData>)

object EmptyRestaurantStreet : RestaurantStreet(emptyList())