package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.Favorite

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