package com.shebang.dog.goo.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.model.Distance
import com.shebang.dog.goo.model.ImageUrl
import com.shebang.dog.goo.model.Name
import com.shebang.dog.goo.model.RestaurantData

class RestaurantCardView(context: Context, attr: AttributeSet) : MaterialCardView(context, attr) {
    private val nameTextView: TextView
    private val distanceTextView: TextView
    private val thumbnailImageView: ImageView
    private val favoriteImageButton: ImageButton

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.restaurant_card_view, this, true)

        nameTextView = findViewById(R.id.name_text_view)
        distanceTextView = findViewById(R.id.distance_text_view)
        thumbnailImageView = findViewById(R.id.thumbnail_image_view)
        favoriteImageButton = findViewById(R.id.favorite_image_button)
    }

    fun setName(name: Name) {
        nameTextView.text = name.value
    }

    fun setDistance(distance: Distance) {
        distanceTextView.text = distance.toString()
    }

    fun setThumbnail(imageUrl: ImageUrl) {
        thumbnailImageView.apply {
            isVisible = imageUrl.stringList.isNotEmpty()

            also {
                if (it.isVisible) {
                    Glide.with(it.context)
                        .load(imageUrl.stringList.first())
                        .into(it)
                }
            }
        }
    }

    fun setFavoriteIcon(
        restaurantData: RestaurantData,
        favorite: Drawable?,
        border: Drawable?,
        onClick: (RestaurantData, ImageButton, Drawable?, Drawable?) -> Unit
    ) {
        favoriteImageButton.apply {
            isSelected = restaurantData.favorite.value

            setImageDrawable(
                if (favoriteImageButton.isSelected) favorite
                else border
            )

            setOnClickListener {
                onClick.invoke(
                    restaurantData,
                    it as ImageButton,
                    favorite,
                    border
                )
            }

        }
    }

    fun removeFavoriteIcon() {
        favoriteImageButton.isVisible = false
    }

    fun hideDistanceTextView() {
        distanceTextView.isVisible = false
    }

}
