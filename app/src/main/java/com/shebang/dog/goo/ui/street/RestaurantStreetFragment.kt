package com.shebang.dog.goo.ui.street

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.Latitude
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.Longitude
import com.shebang.dog.goo.databinding.FragmentRestaurantListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.ui.tab.TabbedFragment
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
    private lateinit var locationRequest: LocationRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantListBinding.bind(view)

        val context = view.context

        if (!checkPermissions(context)) requestPermissions(context)

        restaurantStreetViewModel.restaurantStreet
            .observe(this) { restaurantStreetAdapter.restaurantStreet = it }

        binding.restaurantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantStreetAdapter
        }

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 10000 / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun onResume() {
        super.onResume()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        view?.context?.also { context ->
            locationSettingsClient = LocationServices.getSettingsClient(context)
            locationSettingsClient.checkLocationSettings(builder.build()).addOnSuccessListener {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val location = convertAndroidLocation(it)

                    LocationSharedPreferenceAccessor.setLocationResult(
                        context,
                        location
                    )

                    restaurantStreetViewModel.walkRestaurantStreet(location)
                }

            }

        }
    }

    override fun getTabIconId(): Int {
        return R.drawable.ic_local_dining_black_24dp
    }

    override fun getTabTitle(): String {
        return "FOOD"
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
                    34
                )
            }.show()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                34
            )
        }

    }

    private fun convertAndroidLocation(location: android.location.Location): Location {
        return Location(
            Latitude(location.latitude),
            Longitude(location.longitude)
        )
    }

}
