package com.shebang.dog.goo.data.model

data class Range(val value: Int) {
    init {
        require((1..5).contains(value))
    }
}