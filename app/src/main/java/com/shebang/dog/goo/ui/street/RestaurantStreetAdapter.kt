package com.shebang.dog.goo.ui.street

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.model.EmptyRestaurantStreet
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.RestaurantData
import com.shebang.dog.goo.ui.home.HomeFragmentDirections
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
                    if (it == null) cardView.hideDistanceTextView()
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

            itemView.setOnClickListener {

                val action = HomeFragmentDirections.actionToRestaurantDetail(restaurantData.id)
                it.findNavController().navigate(action)
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