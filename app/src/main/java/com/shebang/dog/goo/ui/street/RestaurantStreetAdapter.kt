package com.shebang.dog.goo.ui.street

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.RestaurantListItemBinding
import com.shebang.dog.goo.model.RestaurantData
import com.shebang.dog.goo.model.RestaurantStreet

class RestaurantStreetAdapter(private val restaurantStreet: RestaurantStreet) :
    RecyclerView.Adapter<RestaurantStreetAdapter.RestaurantStreetViewHolder>() {

    private lateinit var context: Context
    private lateinit var binding: RestaurantListItemBinding

    class RestaurantStreetViewHolder(
        private val binding: RestaurantListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setRestaurantData(restaurantData: RestaurantData, context: Context) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.favorite_animation)
            this.binding.apply {
                this.nameTextView.text = restaurantData.name
            }.also {
                Glide.with(context).load(restaurantData.imageUri).into(it.thumbnailImageView)
                Glide.with(context).load(R.drawable.ic_goo).into(it.favoriteImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStreetViewHolder {
        this.context = parent.context

        with(LayoutInflater.from(this.context)) {
            this@RestaurantStreetAdapter.binding = RestaurantListItemBinding.inflate(this)
        }

        this.binding.apply {
            this.favoriteImageView.setOnClickListener {

            }
        }

        return RestaurantStreetViewHolder(this.binding)
    }

    override fun getItemCount(): Int {
        return this.restaurantStreet.restaurantDataList.size
    }

    override fun onBindViewHolder(holder: RestaurantStreetViewHolder, position: Int) {
        with(this.restaurantStreet) {
            holder.setRestaurantData(
                this.restaurantDataList[position],
                this@RestaurantStreetAdapter.context
            )
        }
    }
}