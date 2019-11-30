package com.shebang.dog.goo.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shebang.dog.goo.model.Id
import com.shebang.dog.goo.model.RestaurantData

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurantData(restaurantData: RestaurantData)

    @Query("SELECT * FROM restaurant_data_table")
    fun getRestaurantStreet(): List<RestaurantData>?

    @Query("DELETE FROM restaurant_data_table")
    fun deleteRestaurantStreet()

    @Query("SELECT * FROM restaurant_data_table WHERE id = :id")
    fun getRestaurantData(id: Id): RestaurantData?
}