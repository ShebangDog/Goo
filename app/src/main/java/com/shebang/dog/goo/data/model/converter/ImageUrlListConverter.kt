package com.shebang.dog.goo.data.model.converter

import androidx.room.TypeConverter

class ImageUrlListConverter {
    @TypeConverter
    fun fromStringList(valueList: List<String>): String {
        return valueList.joinToString(separator)
    }

    @TypeConverter
    fun imageUrlToStringList(imageUrl: String): List<String> {
        return imageUrl.split(separator)
    }

    companion object {
        const val separator = ","
    }
}