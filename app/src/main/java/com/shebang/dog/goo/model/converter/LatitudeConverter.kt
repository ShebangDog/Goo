package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.Latitude

class LatitudeConverter {
    @TypeConverter
    fun fromDouble(value: Double): Latitude {
        return Latitude(value)
    }

    @TypeConverter
    fun longitudeToDouble(latitude: Latitude): Double {
        return latitude.value
    }

}