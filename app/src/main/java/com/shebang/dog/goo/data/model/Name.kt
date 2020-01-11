package com.shebang.dog.goo.data.model

import java.util.*

data class Name(val value: String) {
    fun formalize(): String {
        return value
            .toLowerCase(Locale.ROOT)
            .replace(" ", "")
            .replace("　", "")
    }
}