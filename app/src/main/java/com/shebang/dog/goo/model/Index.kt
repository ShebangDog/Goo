package com.shebang.dog.goo.model

data class Index(private val value: Int) {
    fun toHotpepperValue(dataCount: Int): Int {
        val startValue = (value - 1) * dataCount

        return if (startValue == 0) 1 else startValue + 1
    }

    fun toGurumenaviValue(): Int {
        return value
    }
}