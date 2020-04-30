package com.shebang.dog.goo.model.converter

import androidx.room.TypeConverter
import com.shebang.dog.goo.model.restaurant.Address

class AddressConverter {
    @TypeConverter
    fun fromString(string: String): Address {
        return Address(string)
    }

    @TypeConverter
    fun phoneNumberToString(address: Address): String {
        return address.value
    }
}