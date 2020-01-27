package com.shebang.dog.goo.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shebang.dog.goo.data.model.Id
import com.shebang.dog.goo.data.model.RestaurantData

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurantData(restaurantData: RestaurantData)

    @Query("SELECT * FROM restaurant_data_table")
    fun getRestaurantList(): List<RestaurantData>?

    @Query("DELETE FROM restaurant_data_table")
    fun deleteRestaurantStreet()

    @Query("DELETE FROM restaurant_data_table WHERE id = :id")
    fun deleteRestaurantData(id: Id)

    @Query("SELECT * FROM restaurant_data_table WHERE id = :id")
    fun getRestaurantData(id: Id): RestaurantData?
}