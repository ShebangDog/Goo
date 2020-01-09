package com.shebang.dog.goo.ui.street

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.RestaurantData
import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor

class RestaurantStreetAdapter :
    RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

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

        fun setRestaurantData(restaurantData: RestaurantData) {
            binding.apply {
                nameTextView.text = restaurantData.name.value
                distanceTextView.text = Location.distance(
                    restaurantData.location,
                    LocationSharedPreferenceAccessor.getLocationResult(context)!!
                ).toString()

                Glide.with(thumbnailImageView.context)
                    .load(restaurantData.imageUrl)
                    .into(thumbnailImageView)

                favoriteImageView.setImageDrawable(
                    favoriteImageView.context.getDrawable(R.drawable.ic_goo_border)
                )
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
        holder.setRestaurantData(restaurantStreet.restaurantDataList[position])
    }
}