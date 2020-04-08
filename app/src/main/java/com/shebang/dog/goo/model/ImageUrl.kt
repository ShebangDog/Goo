package com.shebang.dog.goo.model

data class ImageUrl(val stringList: List<String>) {
    infix operator fun plus(other: ImageUrl): ImageUrl {
        return ImageUrl(stringList + other.stringList)
    }
}