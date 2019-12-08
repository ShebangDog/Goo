package com.shebang.dog.goo.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {

        private var restaurantDataBase: RestaurantDatabase? = null

        fun getDataBase(context: Context): RestaurantDatabase? {
            if (restaurantDataBase == null) {
                restaurantDataBase = Room.databaseBuilder(
                    context.applicationContext,
                    RestaurantDatabase::class.java,
                    "restaurant_database"
                ).build()
            }

            return restaurantDataBase
        }
    }
}