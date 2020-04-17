package com.shebang.dog.goo.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.databinding.FavoriteListItemBinding
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.RestaurantData
import com.shebang.dog.goo.ui.home.HomeFragmentDirections
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
        private val binding: FavoriteListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setRestaurantData(restaurantData: RestaurantData) {
            val cardView = binding.cardView

            cardView.apply {
                setName(restaurantData.name)

                restaurantData.location.also {
                    if (it == null) cardView.hideDistanceTextView()
                    else setDistance(
                        Location.distance(
                            it,
                            LocationSharedPreferenceAccessor.getLocationResult(context)!!
                        )
                    )
                }
                setThumbnail(restaurantData.imageUrl)

                removeFavoriteIcon()

                itemView.setOnClickListener {

                    val action = HomeFragmentDirections.actionToRestaurantDetail(restaurantData.id)
                    it.findNavController().navigate(action)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteListItemBinding.inflate(inflater, parent, false)

        return RestaurantStreetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        holder.setRestaurantData(restaurantStreet.restaurantDataList[position])
    }
}