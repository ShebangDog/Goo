package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import java.net.URL

class ImageUrlConverter {
    @TypeConverter
    fun fromString(value: String): URL {
        return URL(value)
    }

    @TypeConverter
    fun imageUrlToString(imageUrl: URL): String {
        return imageUrl.toExternalForm()
    }
}