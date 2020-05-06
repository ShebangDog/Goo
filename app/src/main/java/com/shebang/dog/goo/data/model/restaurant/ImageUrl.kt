package com.shebang.dog.goo.data.model.restaurant

data class ImageUrl(val stringList: List<String>) {
    infix operator fun plus(other: ImageUrl): ImageUrl {
        return ImageUrl(stringList + other.stringList)
    }
}