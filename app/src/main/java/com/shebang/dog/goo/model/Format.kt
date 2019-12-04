package com.shebang.dog.goo.model

sealed class Format {

    object Json : Format() {
        const val value = "json"
    }

    object JsonP : Format() {
        const val value = "jsonp"
    }

    object Xml : Format() {
        const val value = "xml"
    }
}