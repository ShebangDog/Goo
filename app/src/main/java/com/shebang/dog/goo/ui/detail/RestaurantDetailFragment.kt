package com.shebang.dog.goo.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding

class RestaurantDetailFragment : Fragment(R.layout.fragment_restaurant_detail) {
    private lateinit var binding: FragmentRestaurantDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantDetailBinding.bind(view)

        val args: RestaurantDetailFragmentArgs by navArgs()

        binding.apply {
            args.also {
                restaurantNameTextView.text = it.name
                with(thumbnailImageView) { Glide.with(this).load(it.thumbnail).into(this) }
            }
        }
    }
}