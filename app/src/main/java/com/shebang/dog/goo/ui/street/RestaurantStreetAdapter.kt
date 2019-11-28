package com.shebang.dog.goo.ui.street

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.model.Location
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

class RestaurantStreetAdapter(private val restaurantStreet: RestaurantStreet) :
    RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

    private lateinit var binding: RestaurantListItemBinding
    private lateinit var context: Context

    class RestaurantStreetViewHolder(
        private val binding: RestaurantListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setRestaurantData(restaurantData: RestaurantData) {
            this.binding.apply {
                nameTextView.text = restaurantData.name
                thumbnailImageView = TODO("restaurantData.imageUrl")
                favoriteImageView = TODO("resource")
                distanceTextView.text =
                    Location.distance(restaurantData.location, TODO()).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        binding = RestaurantListItemBinding.inflate(inflater)

        with(LayoutInflater.from(parent.context)) {
            binding = RestaurantListItemBinding.inflate(this)
        }

        return RestaurantStreetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        with(restaurantStreet) {
            holder.setRestaurantData(restaurantDataList[position])
        }
    }
}