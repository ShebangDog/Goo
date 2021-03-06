package com.shebang.dog.goo.ui.main.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.restaurant.PhoneNumber
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ui.base.MyDaggerFragment
import com.shebang.dog.goo.util.GoogleMapUtil
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
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

            mapCardView.setOnIconClickListener {
                val userLocation = LocationSharedPreferenceAccessor.getLocationResult(view.context)
                restaurantDetailViewModel.navigateToRestaurant(userLocation) {
                    openWithGoogleMap(it)
                }
            }

            phoneCardView.setOnIconClickListener {
                restaurantDetailViewModel.callingRestaurant { dialNumber(it) }
            }
        }

        restaurantDetailViewModel.restaurantImageUrlList.observe(viewLifecycleOwner) {
            restaurantThumbnailAdapter.restaurantThumbnailList = it
        }
    }

    private fun dialNumber(phoneNumber: PhoneNumber) {
        if (phoneNumber.value.isBlank()) return

        val dialIntent = createDialIntent(Uri.parse("tel:${phoneNumber.value}"))

        if (dialIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(dialIntent)
        }
    }

    private fun createDialIntent(uri: Uri): Intent {
        return Intent(Intent.ACTION_DIAL).apply { data = uri }
    }

    private fun openWithGoogleMap(uri: String) {
        val mapIntent = createMapIntent(uri)

        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun createMapIntent(uri: String): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uri)
        ).apply { setPackage(GoogleMapUtil.PackageName) }
    }
}