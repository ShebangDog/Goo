package com.shebang.dog.goo.data.model.restaurant

import java.util.*

data class Name(val value: String) {
    fun formalize(): String {
        return value
            .toLowerCase(Locale.ROOT)
            .replace(" ", "")
            .replace("　", "")
    }

    infix operator fun plus(other: Name): Name {
        return Name(value + other.value)
    }
}