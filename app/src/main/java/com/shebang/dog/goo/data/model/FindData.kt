package com.shebang.dog.goo.data.model

sealed class FindData<T> {
    data class Found<T>(val value: T) : FindData<T>()

    class NotFound<T> : FindData<T>()

    fun <R> map(function: (T) -> (R)): FindData<R> {
        return plan(
            { NotFound() },
            { Found(function.invoke(it)) }
        )
    }

    fun ifFound(function: (T) -> Unit) {
        plan(
            { this },
            { function(it) }
        )
    }

    fun orElse(defaultValue: T): T {
        return plan({ defaultValue }, { it })
    }

    private fun <R> plan(ifNotFound: () -> R, ifFound: (T) -> R): R {
        return when (this) {
            is NotFound -> ifNotFound.invoke()
            is Found -> ifFound.invoke(value)
        }
    }
}