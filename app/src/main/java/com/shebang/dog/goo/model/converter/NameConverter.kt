package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.Name

class NameConverter {
    @TypeConverter
    fun fromString(value: String): Name {
        return Name(value)
    }

    @TypeConverter
    fun nameToString(name: Name): String {
        return name.value
    }
}