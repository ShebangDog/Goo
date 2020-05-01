package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.restaurant.PhoneNumber

class PhoneNumberConverter {
    @TypeConverter
    fun fromString(string: String): PhoneNumber {
        return PhoneNumber(string)
    }

    @TypeConverter
    fun phoneNumberToString(phoneNumber: PhoneNumber): String {
        return phoneNumber.value
    }
}