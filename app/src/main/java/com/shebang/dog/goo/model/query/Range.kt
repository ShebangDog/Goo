package com.shebang.dog.goo.model.query

import com.shebang.dog.goo.model.restaurant.Distance

data class Range(val value: Int) {
    init {
        require((1..5).contains(value))
    }

    fun toDistance(): Distance {
        return distanceList[value - 1]
    }

    companion object {
        private val distanceList =
            listOf(300, 500, 1000, 2000, 3000).map {
                Distance(
                    it.toDouble()
                )
            }
    }
}