package com.shebang.dog.goo.data.model.query

data class Index(private val value: Int) {
    init {
        require(0 <= value)
    }

    fun toHotpepperValue(dataCount: Int): Int {
        val startValue = (value - 1) * dataCount

        return if (startValue == 0) 1 else startValue + 1
    }

    fun toGurumenaviValue(): Int {
        return value
    }
}