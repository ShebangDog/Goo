package com.shebang.dog.goo.data.model

import android.view.View

data class AboutItem(
    val title: Title,
    val summary: Summary? = null,
    val onClick: ((View) -> Unit)? = null
)