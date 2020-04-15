package com.shebang.dog.goo.model.restaurant

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shebang.dog.goo.model.location.Location

@Entity(tableName = "restaurant_data_table")
data class RestaurantData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Id,

    @ColumnInfo(name = "name")
    val name: Name,

    @ColumnInfo(name = "image_url")
    val imageUrl: ImageUrl,

    @Embedded
    val location: Location?,

    @ColumnInfo(name = "favorite")
    var favorite: Favorite
) {

    fun switchFavorite() {
        favorite = favorite.switch()
    }

    infix operator fun plus(other: RestaurantData): RestaurantData {
        require(name.formalize() == other.name.formalize())

        return RestaurantData(
            id,
            Name(name.value),
            imageUrl + other.imageUrl,
            location,
            other.favorite or favorite
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}