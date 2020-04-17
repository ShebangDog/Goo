package com.shebang.dog.goo.ui.street

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.model.Index
import com.shebang.dog.goo.model.location.Latitude
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.location.Longitude
import com.shebang.dog.goo.ui.tab.TabbedFragment
import com.shebang.dog.goo.util.EndlessRecyclerViewScrollListener
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.PermissionGranter
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override val tabIconId: Int
        get() = R.drawable.ic_local_dining_black_24dp

    override val tabTitle: String
        get() = "FOOD"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)

        restaurantStreetViewModel.restaurantStreet.observe(viewLifecycleOwner) {
            restaurantStreetAdapter.restaurantStreet = it
        }

        binding.progressBar.show()
        restaurantStreetViewModel.loadingState.observe(viewLifecycleOwner) {
            binding.progressBar.apply { if (it) show() else hide() }
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

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()

        context?.also { context ->
            if (restaurantStreetViewModel.isEmptyRestaurantStreet() &&
                PermissionGranter.checkPermissions(context)
            ) {

                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val location = convertAndroidLocation(it ?: return@addOnSuccessListener)
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
}