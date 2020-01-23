package com.shebang.dog.goo.ui.street

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.RestaurantData
import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor

class RestaurantStreetAdapter(
    private val action: (RestaurantData, ImageButton, Drawable?, Drawable?) -> Unit
) : RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

    var restaurantStreet: RestaurantStreet = RestaurantStreet(emptyList())
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
            action: (RestaurantData, ImageButton, Drawable?, Drawable?) -> Unit
        ) {
            binding.apply {
                nameTextView.text = restaurantData.name.value
                distanceTextView.text = Location.distance(
                    restaurantData.location,
                    LocationSharedPreferenceAccessor.getLocationResult(context)!!
                ).toString()

                Glide.with(thumbnailImageView.context)
                    .load(restaurantData.imageUrl.random())
                    .into(thumbnailImageView)

                favoriteImageButton.apply {
                    isSelected = restaurantData.favorite.value

                    setImageDrawable(
                        if (favoriteImageButton.isSelected) favorite
                        else border
                    )

                    setOnClickListener {
                        action.invoke(
                            restaurantData,
                            it as ImageButton,
                            favorite,
                            border
                        )
                    }

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
        holder.setRestaurantData(
            restaurantStreet.restaurantDataList[position],
            action
        )
    }
}