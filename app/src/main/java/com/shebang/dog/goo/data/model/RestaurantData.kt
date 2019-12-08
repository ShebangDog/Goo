package com.shebang.dog.goo.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_data_table")
data class RestaurantData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Id,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @Embedded
    val location: Location
)