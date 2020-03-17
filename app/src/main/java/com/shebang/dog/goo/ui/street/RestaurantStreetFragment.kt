package com.shebang.dog.goo.ui.street

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.Index
import com.shebang.dog.goo.data.model.Latitude
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.Longitude
import com.shebang.dog.goo.databinding.FragmentRestaurantListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.ui.tab.TabbedFragment
import com.shebang.dog.goo.util.EndlessRecyclerViewScrollListener
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.PermissionGranter
import javax.inject.Inject

class RestaurantStreetFragment : TabbedFragment(R.layout.fragment_restaurant_list) {
    @Inject
    lateinit var restaurantStreetViewModelFactory: ViewModelFactory
    private val restaurantStreetViewModel by assistedViewModels {
        restaurantStreetViewModelFactory.create(RestaurantStreetViewModel::class.java)
    }

    @Inject
    lateinit var restaurantStreetAdapter: RestaurantStreetAdapter

    private lateinit var binding: FragmentRestaurantListBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override val tabIconId: Int
        get() = R.drawable.ic_local_dining_black_24dp

    override val tabTitle: String
        get() = "FOOD"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantListBinding.bind(view)
        linearLayoutManager = LinearLayoutManager(context)

        restaurantStreetViewModel.restaurantStreet.observe(this) {
            restaurantStreetAdapter.restaurantStreet = it
            binding.progressBar.isVisible = !isShownRecyclerViewItem()
        }

        binding.restaurantListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = restaurantStreetAdapter

            addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    currentLocation?.also {
                        restaurantStreetViewModel.walkRestaurantStreet(it, Index(page + 1))
                    }
                }
            })
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
    }

    override fun onResume() {
        super.onResume()

        context?.also { context ->
            if (!isShownRecyclerViewItem() && PermissionGranter.checkPermissions(context)) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val location = convertAndroidLocation(it)
                    currentLocation = location
                    LocationSharedPreferenceAccessor.setLocationResult(context, location)

                    restaurantStreetViewModel.walkRestaurantStreet(location)
                }

            }
        }
    }

    private fun convertAndroidLocation(location: android.location.Location): Location {
        return Location(
            Latitude(location.latitude),
            Longitude(location.longitude)
        )
    }

    private fun isShownRecyclerViewItem(): Boolean {
        return linearLayoutManager.findFirstVisibleItemPosition() != NO_POSITION
    }

}