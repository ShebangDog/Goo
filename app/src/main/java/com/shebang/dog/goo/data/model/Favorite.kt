package com.shebang.dog.goo.data.model

data class Favorite(val value: Boolean) {
    fun switch(): Favorite {
        return Favorite(!value)
    }
}
