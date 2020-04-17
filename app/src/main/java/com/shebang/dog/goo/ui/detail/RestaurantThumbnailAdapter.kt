package com.shebang.dog.goo.ui.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.RestaurantDetailThumbnailBinding

class RestaurantThumbnailAdapter :
    RecyclerView.Adapter<RestaurantThumbnailAdapter.RestaurantThumbnailViewHolder>() {

    var restaurantThumbnailList: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class RestaurantThumbnailViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RestaurantDetailThumbnailBinding.bind(view)

        fun setThumbnail(imageUrl: String) {
            binding.thumbnailImageView.also {
                binding.progressBar.show()
                Glide.with(view)
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
        val inflate = LayoutInflater.from(parent.context).inflate(
            R.layout.restaurant_detail_thumbnail,
            parent,
            false
        )

        return RestaurantThumbnailViewHolder(inflate)
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