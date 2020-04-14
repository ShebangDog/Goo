package com.shebang.dog.goo.model

import com.shebang.dog.goo.util.DebugHelper

data class Favorite(val value: Boolean) {
    fun switch(): Favorite {
        DebugHelper.log("onSwitch")
        return Favorite(!value)
    }

    infix fun or(other: Favorite): Favorite {
        return Favorite(other.value || this.value)
    }
}
