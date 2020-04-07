package com.shebang.dog.goo.ui.street

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.databinding.RestaurantCardViewBinding
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import javax.inject.Inject

class RestaurantStreetAdapter @Inject constructor(
    private val restaurantStreetViewModel: RestaurantStreetViewModel
) : RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

    var restaurantStreet: RestaurantStreet = EmptyRestaurantStreet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class RestaurantStreetViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        private val binding = RestaurantListItemBinding.bind(view)
        private val context = view.context

        private val border = context.getDrawable(R.drawable.ic_favorite_border_pink_24dp)
        private val favorite = context.getDrawable(R.drawable.ic_favorite_pink_24dp)

        fun setRestaurantData(
            restaurantData: RestaurantData,
            onClick: (RestaurantData, ImageButton, Drawable?, Drawable?) -> Unit
        ) {
            val cardView = binding.cardView

            cardView.apply {
                setName(restaurantData.name)

                restaurantData.location.also {
                    if (it == null) cardView.distanceTextView.isVisible = false
                    else setDistance(
                        Location.distance(
                            it,
                            LocationSharedPreferenceAccessor.getLocationResult(context)!!
                        )
                    )
                }

                setThumbnail(restaurantData.imageUrl)

                setFavoriteIcon(restaurantData, favorite, border, onClick)
            }
        }

        private fun RestaurantCardViewBinding.setName(name: Name) {
            nameTextView.text = name.value
        }

        private fun RestaurantCardViewBinding.setDistance(distance: Distance) {
            distanceTextView.text = distance.toString()
        }

        private fun RestaurantCardViewBinding.setThumbnail(imageUrl: ImageUrl) {
            thumbnailImageView.isVisible = imageUrl.stringList.isNotEmpty()

            thumbnailImageView.also {
                if (it.isVisible) {
                    Glide.with(it.context)
                        .load(imageUrl.stringList.first())
                        .into(it)
                }
            }
        }

        private fun RestaurantCardViewBinding.setFavoriteIcon(
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_item, parent, false)

        return RestaurantStreetViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        holder.setRestaurantData(restaurantStreet.restaurantDataList[position]) { restaurantData, imageButton, favorite, border ->
            imageButton.isSelected = !imageButton.isSelected

            restaurantStreetViewModel.toggleFavorite(restaurantData, imageButton, favorite, border)
        }
    }
}