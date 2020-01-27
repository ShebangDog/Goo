package com.shebang.dog.goo.data.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.data.model.Longitude

class LongitudeConverter {
    @TypeConverter
    fun fromDouble(value: Double): Longitude {
        return Longitude(value)
    }

    @TypeConverter
    fun longitudeToDouble(longitude: Longitude): Double {
        return longitude.value
    }
}