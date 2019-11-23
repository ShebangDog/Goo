package com.shebang.dog.goo.ui.street

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

    class RestaurantStreetViewHolder(
        private val binding: RestaurantListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setRestaurantData(restaurantData: RestaurantData) {
            this.binding.apply {
                this.nameTextView.text = restaurantData.name
                this.thumbnailImageView = TODO() // restaurantData.imageUrl
                this.favoriteImageView = TODO() // resource
                this.distanceTextView.text =
                    Location.distance(restaurantData.location, TODO()).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        with(LayoutInflater.from(parent.context)) {
            this@RestaurantStreetAdapter.binding = RestaurantListItemBinding.inflate(this)
        }

        return RestaurantStreetViewHolder(this.binding)
    }

    override fun getItemCount(): Int {
        return this.restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        with(this.restaurantStreet) {
            holder.setRestaurantData(this.restaurantDataList[position])
        }
    }
}