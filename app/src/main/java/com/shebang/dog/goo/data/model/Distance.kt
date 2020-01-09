package com.shebang.dog.goo.data.model

data class Distance(val value: Double) {
    override fun toString(): String {
        return "ここから ${value.toInt()} m"
    }
}
