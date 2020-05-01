package com.shebang.dog.goo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ui.tab.MyDaggerFragment
import javax.inject.Inject

class RestaurantDetailFragment : MyDaggerFragment(R.layout.fragment_restaurant_detail) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val restaurantDetailViewModel by activityViewModels<RestaurantDetailViewModel> { viewModelFactory }

    private lateinit var binding: FragmentRestaurantDetailBinding
    private val restaurantThumbnailAdapter = RestaurantThumbnailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = restaurantDetailViewModel
            lifecycleOwner = viewLifecycleOwner

            viewPager.adapter = restaurantThumbnailAdapter
            dotsIndicator.setViewPager2(viewPager)
        }

        restaurantDetailViewModel.restaurantImageUrlList.observe(viewLifecycleOwner) {
            restaurantThumbnailAdapter.restaurantThumbnailList = it
        }
    }
}