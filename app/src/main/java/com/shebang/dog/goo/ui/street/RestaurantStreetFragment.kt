package com.shebang.dog.goo.ui.street

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var locationSettingsClient: SettingsClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest by lazy { createLocationRequest() }

    private var currentLocation: Location? = null

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 34
    }

    override val tabIconId: Int
        get() = R.drawable.ic_local_dining_black_24dp

    override val tabTitle: String
        get() = "FOOD"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantListBinding.bind(view)

        val context = view.context

        if (!checkPermissions(context)) requestPermissions(context)

        restaurantStreetViewModel.restaurantStreet.observe(this) {
            restaurantStreetAdapter.restaurantStreet = it
            binding.progressBar.isVisible = it.restaurantDataList.isEmpty()
        }

        val linearLayoutManager = LinearLayoutManager(context)
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        locationSettingsClient = LocationServices.getSettingsClient(context)
        locationSettingsClient.checkLocationSettings(builder.build()).addOnSuccessListener {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val location = convertAndroidLocation(it)
                currentLocation = location

                LocationSharedPreferenceAccessor.setLocationResult(
                    context,
                    location
                )

                restaurantStreetViewModel.walkRestaurantStreet(location)
            }

        }

    }

    private fun checkPermissions(context: Context): Boolean {
        val fineLocationPermissionState = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocationPermissionState = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val isGranted = PackageManager.PERMISSION_GRANTED

        return fineLocationPermissionState == isGranted && coarseLocationPermissionState == isGranted
    }

    private fun requestPermissions(context: Context) {
        val isGranted = PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == isGranted

        val permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == isGranted

        val shouldProvideRationale =
            permissionAccessFineLocationApproved && permissionAccessCoarseLocationApproved

        if (shouldProvideRationale) {
            Snackbar.make(
                binding.restaurantListFragment,
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                // Request permission
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }.show()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

    }

    private fun convertAndroidLocation(location: android.location.Location): Location {
        return Location(
            Latitude(location.latitude),
            Longitude(location.longitude)
        )
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 10000 / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}
