package com.shebang.dog.goo.data.model

import java.util.*

data class Name(val value: String) {

    override fun hashCode(): Int {
        return value
            .toLowerCase(Locale.ROOT)
            .replace(" ", "")
            .replace("　", "").hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Name

        if (value != other.value) return false

        return true
    }
}