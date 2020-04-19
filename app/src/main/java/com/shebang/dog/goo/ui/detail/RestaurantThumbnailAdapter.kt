package com.shebang.dog.goo.ui.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.shebang.dog.goo.databinding.RestaurantDetailThumbnailBinding

class RestaurantThumbnailAdapter :
    RecyclerView.Adapter<RestaurantThumbnailAdapter.RestaurantThumbnailViewHolder>() {

    var restaurantThumbnailList: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class RestaurantThumbnailViewHolder(private val binding: RestaurantDetailThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setThumbnail(imageUrl: String) {
            binding.thumbnailImageView.also {
                binding.progressBar.show()
                Glide.with(binding.root)
                    .load(imageUrl)
                    .listener(GlideListener(binding.progressBar))
                    .into(it)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantThumbnailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RestaurantDetailThumbnailBinding.inflate(inflater, parent, false)

        return RestaurantThumbnailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantThumbnailList.size
    }

    override fun onBindViewHolder(holder: RestaurantThumbnailViewHolder, position: Int) {
        holder.setThumbnail(restaurantThumbnailList[position])
    }

    class GlideListener(private val progressBar: ContentLoadingProgressBar) :
        RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar.hide()
            return false
        }
    }
}