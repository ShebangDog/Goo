package com.shebang.dog.goo.model.restaurant

import kotlin.math.pow

data class Distance(val value: Double) : Comparable<Distance> {
    init {
        require(0 <= value)
    }

    private companion object {
        val METRICS_PREFIXES = setOf("", "k").toList()
        const val POSTFIX = "m"
    }

    override fun toString(): String {
        val result = unitConversion()
        val distance = WrappedDouble(
            result.first.toDouble()
        )
            .let { it.take(3, if (it.integerDigits < 2) 1 else 0) }

        val unit = result.second + POSTFIX

        return "ここから $distance$unit"
    }

    override fun compareTo(other: Distance): Int {
        return value.compareTo(other.value)
    }

    private fun unitConversion(): Pair<Number, String> {
        return WrappedDouble(value)
            .let {
                val index = (it.integerDigits - 3).takeUnless { len -> len <= 0 }
                    ?.let { len -> len / 4 + 1 } ?: 0

                val divider = 1000.toDouble().pow(index.toDouble())
                val number = if (index == 0) value else value / divider
                val prefix = METRICS_PREFIXES.getOrNull(index) ?: METRICS_PREFIXES.first()

                Pair(number, prefix)
            }
    }

    private class WrappedDouble(value: Double) {
        private val integer =
            WrappedInt(value.toInt())
        private val decimal =
            Decimal(
                value - integer.value
            )

        val integerDigits = integer.digits
        val decimalDigits = decimal.digits
        val digits = integerDigits + decimalDigits

        fun take(integerLength: Int, decimalLength: Int): String {
            val front = integer.take(integerLength)
            val back = decimal.take(decimalLength)

            return front + (if (back.isNotBlank()) "." else "") + back
        }

        private class Decimal(private val value: Double) {
            init {
                require(value < 1.0)
            }

            private val valueString by lazy { value.toString() }
            val digits = valueString.length - 2

            fun take(n: Int): String {
                return valueString.drop(2).take(n)
            }
        }
    }

    private class WrappedInt(val value: Int) {
        private val valueString by lazy { value.toString() }
        val digits = valueString.length

        fun take(n: Int): String {
            return valueString.take(n)
        }
    }
}