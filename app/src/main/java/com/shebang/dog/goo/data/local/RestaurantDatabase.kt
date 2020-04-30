package com.shebang.dog.goo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shebang.dog.goo.model.converter.*
import com.shebang.dog.goo.model.restaurant.RestaurantData

@Database(entities = [RestaurantData::class], version = 1)
@TypeConverters(
    RestaurantStreetConverter::class,
    IdConverter::class,
    NameConverter::class,
    ImageUrlConverter::class,
    LatitudeConverter::class,
    LongitudeConverter::class,
    FavoriteConverter::class,
    AddressConverter::class,
    PhoneNumberConverter::class
)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}