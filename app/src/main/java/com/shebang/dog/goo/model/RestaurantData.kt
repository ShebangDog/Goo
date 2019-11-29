package com.shebang.dog.goo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "restaurant_data_table")
data class RestaurantData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Id,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: URL,

    @ColumnInfo(name = "location")
    val location: Location
)