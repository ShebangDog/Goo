package com.shebang.dog.goo.data.model.restaurant

import androidx.core.text.isDigitsOnly

data class PhoneNumber(val value: String) {
    init {
        require(isPhoneNumber(value))
    }

    private fun isPhoneNumber(value: String): Boolean {
        val excludedString = value.filter { it != '-' }
        return excludedString.isDigitsOnly()
    }
}
