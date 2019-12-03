package com.shebang.dog.goo.model

sealed class FindData<T> {
    data class Found<T>(val value: T) : FindData<T>()

    class NotFound<T> : FindData<T>()
}