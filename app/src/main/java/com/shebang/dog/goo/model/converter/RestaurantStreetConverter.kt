package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.restaurant.RestaurantData

class RestaurantStreetConverter {
    @TypeConverter
    fun fromRestaurantList(restaurantDataList: List<RestaurantData>): RestaurantStreet {
        return RestaurantStreet(restaurantDataList)
    }

    @TypeConverter
    fun restaurantStreetToRestaurantList(restaurantStreet: RestaurantStreet): List<RestaurantData> {
        return restaurantStreet.restaurantDataList
    }
}