package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.restaurant.ImageUrl

class ImageUrlConverter {
    @TypeConverter
    fun fromImageUrl(value: ImageUrl): String {
        return value.stringList.joinToString(separator)
    }

    @TypeConverter
    fun stringToImageUrl(string: String): ImageUrl {
        return ImageUrl(
            string.split(
                separator
            ).filter { it.isNotBlank() })
    }

    companion object {
        const val separator = ","
    }
}