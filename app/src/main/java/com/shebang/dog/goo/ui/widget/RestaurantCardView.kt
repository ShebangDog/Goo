package com.shebang.dog.goo.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.shebang.dog.goo.databinding.RestaurantCardViewBinding
import com.shebang.dog.goo.model.restaurant.Distance
import com.shebang.dog.goo.model.restaurant.ImageUrl
import com.shebang.dog.goo.model.restaurant.Name
import com.shebang.dog.goo.model.restaurant.RestaurantData

class RestaurantCardView(context: Context, attr: AttributeSet) : MaterialCardView(context, attr) {
    private val binding: RestaurantCardViewBinding

    init {
        val inflater = LayoutInflater.from(context)

        binding = RestaurantCardViewBinding.inflate(inflater, this, true)
    }

    fun setName(name: Name) {
        binding.nameTextView.text = name.value
    }

    fun setDistance(distance: Distance) {
        binding.distanceTextView.text = distance.toString()
    }

    fun setThumbnail(imageUrl: ImageUrl) {
        binding.thumbnailImageView.apply {
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
        binding.favoriteImageButton.apply {
            isSelected = restaurantData.favorite.value

            setImageDrawable(
                if (isSelected) favorite
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
        binding.favoriteImageButton.isVisible = false
    }

    fun hideDistanceTextView() {
        binding.distanceTextView.isVisible = false
    }

}
