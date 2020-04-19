package com.shebang.dog.goo.ui.detail

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class RestaurantThumbnailViewModel : ViewModel() {
    fun showThumbnail(thumbnailView: ImageView, imageUrl: String) {
        Glide.with(thumbnailView)
            .load(imageUrl)
            .into(thumbnailView)
    }
}