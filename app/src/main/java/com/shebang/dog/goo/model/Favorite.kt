package com.shebang.dog.goo.model

data class Favorite(val value: Boolean) {
    fun switch(): Favorite {
        return Favorite(!value)
    }
}
