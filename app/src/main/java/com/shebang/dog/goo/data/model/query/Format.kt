package com.shebang.dog.goo.data.model.query

sealed class Format {

    object Json : Format()

    object JsonP : Format()

    object Xml : Format()

    val value by lazy {
        when (this) {
            is Json -> "json"
            is JsonP -> "jsonp"
            is Xml -> "xml"
        }
    }
}