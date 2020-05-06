package com.shebang.dog.goo.ui.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.shebang.dog.goo.data.model.restaurant.Distance
import com.shebang.dog.goo.data.model.restaurant.ImageUrl
import com.shebang.dog.goo.data.model.restaurant.Name
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import com.shebang.dog.goo.databinding.RestaurantCardViewBinding

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
        onClickFavoriteIconListener: OnClickFavoriteIconListener?
    ) {
        binding.favoriteImageButton.apply {
            isSelected = restaurantData.favorite.value

            setImageDrawable(
                if (isSelected) favorite
                else border
            )

            setOnClickListener {
                onClickFavoriteIconListener?.onClickFavoriteIcon(
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

    interface OnClickFavoriteIconListener {
        fun onClickFavoriteIcon(
            restaurantData: RestaurantData,
            imageButton: ImageButton,
            favorite: Drawable?,
            border: Drawable?
        )
    }

    interface OnClickListener {
        fun onClick(
            view: View,
            restaurantData: RestaurantData
        )
    }

    companion object {
        fun OnClickFavoriteIconListener(
            onClickFavoriteIcon: (
                restaurantData: RestaurantData,
                imageButton: ImageButton,
                favorite: Drawable?,
                border: Drawable?
            ) -> Unit
        ): OnClickFavoriteIconListener {
            return object : OnClickFavoriteIconListener {
                override fun onClickFavoriteIcon(
                    restaurantData: RestaurantData,
                    imageButton: ImageButton,
                    favorite: Drawable?,
                    border: Drawable?
                ) {
                    onClickFavoriteIcon(restaurantData, imageButton, favorite, border)
                }
            }
        }

        fun OnClickListener(onClick: (view: View, restaurantData: RestaurantData) -> Unit): OnClickListener {
            return object : OnClickListener {
                override fun onClick(view: View, restaurantData: RestaurantData) {
                    onClick(view, restaurantData)
                }
            }
        }
    }
}
