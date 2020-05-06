package com.shebang.dog.goo.data.model.about

import android.view.View

data class AboutItem(
    val title: Title,
    val summary: Summary? = null,
    val onClick: (View) -> Unit = {}
)