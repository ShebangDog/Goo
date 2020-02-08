package com.shebang.dog.goo.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.databinding.FavoriteListItemBinding
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import javax.inject.Inject

class FavoriteAdapter @Inject constructor(
    private val favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<FavoriteAdapter.RestaurantStreetViewHolder>() {

    var restaurantStreet: RestaurantStreet = RestaurantStreet(emptyList())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class RestaurantStreetViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        private val binding = FavoriteListItemBinding.bind(view)
        private val context = view.context

        fun setRestaurantData(restaurantData: RestaurantData) {
            binding.apply {
                setName(restaurantData.name)

                setDistance(
                    Location.distance(
                        restaurantData.location,
                        LocationSharedPreferenceAccessor.getLocationResult(context)!!
                    )
                )

                setThumbnail(restaurantData.imageUrl)
            }

        }

        private fun FavoriteListItemBinding.setName(name: Name) {
            nameTextView.text = name.value
        }

        private fun FavoriteListItemBinding.setDistance(distance: Distance) {
            distanceTextView.text = distance.toString()
        }

        private fun FavoriteListItemBinding.setThumbnail(imageUrl: ImageUrl) {
            thumbnailImageView.isVisible = imageUrl.stringList.isNotEmpty()

            thumbnailImageView.also {
                if (it.isVisible) {
                    Glide.with(it.context)
                        .load(imageUrl.stringList.first())
                        .into(it)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_list_item, parent, false)

        return RestaurantStreetViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        holder.setRestaurantData(restaurantStreet.restaurantDataList[position])
    }
}