package com.shebang.dog.goo.model

import android.net.Uri

data class RestaurantData (
    val id: Id,
    val name: String,
    val imageUri: Uri,
    val location: Location
)