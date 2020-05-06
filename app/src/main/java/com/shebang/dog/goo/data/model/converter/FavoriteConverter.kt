package com.shebang.dog.goo.data.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.data.model.restaurant.Favorite

class FavoriteConverter {
    @TypeConverter
    fun fromBoolean(value: Boolean): Favorite {
        return Favorite(value)
    }

    @TypeConverter
    fun favoriteToString(favorite: Favorite): Boolean {
        return favorite.value
    }
}