package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

class RestaurantStreetConverter {
    @TypeConverter
    fun fromRestaurantStreet(restaurantStreet: RestaurantStreet): List<RestaurantData> {
        return restaurantStreet.restaurantDataList
    }

    @TypeConverter
    fun restaurantDataListToRestaurantStreet(restaurantDataList: List<RestaurantData>): RestaurantStreet {
        return RestaurantStreet(restaurantDataList)
    }
}