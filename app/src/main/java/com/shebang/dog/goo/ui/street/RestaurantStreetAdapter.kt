package com.shebang.dog.goo.ui.street

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.model.EmptyRestaurantStreet
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.RestaurantData
import com.shebang.dog.goo.ui.widget.RestaurantCardView
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import javax.inject.Inject

class RestaurantStreetAdapter @Inject constructor() :
    RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

    var restaurantStreet: RestaurantStreet = EmptyRestaurantStreet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClickFavoriteIconListener: RestaurantCardView.OnClickFavoriteIconListener? = null
    var onClickListener: RestaurantCardView.OnClickListener? = null

    class RestaurantStreetViewHolder(
        private val binding: RestaurantListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context
        private val border = context.getDrawable(R.drawable.ic_favorite_border_pink_24dp)
        private val favorite = context.getDrawable(R.drawable.ic_favorite_pink_24dp)

        fun setRestaurantData(
            restaurantData: RestaurantData,
            onClickFavoriteIconListener: RestaurantCardView.OnClickFavoriteIconListener?,
            onClickListener: RestaurantCardView.OnClickListener?
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

                setFavoriteIcon(restaurantData, favorite, border, onClickFavoriteIconListener)
            }

            itemView.setOnClickListener { onClickListener?.onClick(it, restaurantData) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RestaurantListItemBinding.inflate(inflater, parent, false)

        return RestaurantStreetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        holder.setRestaurantData(
            restaurantStreet.restaurantDataList[position],
            onClickFavoriteIconListener,
            onClickListener
        )
    }
}