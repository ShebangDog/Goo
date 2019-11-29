package com.shebang.dog.goo.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

@Dao
interface RestaurantDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurantData(restaurantData: RestaurantData)

    @Query("SELECT * FROM restaurant_data_table")
    fun getRestaurantStreet(): RestaurantStreet

    @Query("DELETE FROM restaurant_data_table")
    fun deleteRestaurantStreet()

    @Query("SELECT * FROM restaurant_data_table WHERE id = :id")
    fun getRestaurantData(id: Id): RestaurantStreet
}