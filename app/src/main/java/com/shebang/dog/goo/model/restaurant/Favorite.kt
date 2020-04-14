package com.shebang.dog.goo.model.restaurant

data class Favorite(val value: Boolean) {
    fun switch(): Favorite {
        return Favorite(!value)
    }

    infix fun or(other: Favorite): Favorite {
        return Favorite(other.value || this.value)
    }
}
