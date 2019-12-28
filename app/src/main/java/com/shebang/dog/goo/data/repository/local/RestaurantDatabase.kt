package com.shebang.dog.goo.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shebang.dog.goo.data.model.RestaurantData
import com.shebang.dog.goo.data.model.converter.*

@Database(entities = [RestaurantData::class], version = 1)
@TypeConverters(
    RestaurantStreetConverter::class,
    IdConverter::class,
    ImageUrlConverter::class,
    LatitudeConverter::class,
    LongitudeConverter::class
)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}