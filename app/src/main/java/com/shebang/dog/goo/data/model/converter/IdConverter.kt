package com.shebang.dog.goo.data.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.data.model.restaurant.Id


class IdConverter {
    @TypeConverter
    fun fromString(value: String): Id {
        return Id(value)
    }

    @TypeConverter
    fun idToString(id: Id): String {
        return id.value
    }
}