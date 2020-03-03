package com.shebang.dog.goo.ui.street

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.Latitude
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.Longitude
import com.shebang.dog.goo.databinding.FragmentRestaurantListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.service.LocationBroadCastReceiver
import com.shebang.dog.goo.ui.tab.TabbedFragment
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor.KEY_LOCATION_RESULT
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

    private var currentLocation: Location? = null

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

        LocationSharedPreferenceAccessor.registerOnSharedPreferenceChangeListener(context) {
            LocationSharedPreferenceAccessor.getLocationResult(context)?.also { location ->
                if (it == KEY_LOCATION_RESULT) {
                    currentLocation = location
                    restaurantStreetViewModel.walkRestaurantStreet(location)
                }
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.apply {
            val locationRequest = LocationRequest.create().apply {
                interval = 18000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            lastLocation.addOnSuccessListener {
                val location = Location(
                    Latitude(it.latitude),
                    Longitude(it.longitude)
                )

                LocationSharedPreferenceAccessor.setLocationResult(context, location)
            }

            lastLocation.addOnFailureListener {
                LocationSharedPreferenceAccessor.getLocationResult(context)?.also {
                    restaurantStreetViewModel.walkRestaurantStreet(it)
                }
            }

            requestLocationUpdates(locationRequest, createPendingIntent(context))
        }

    }

    override fun getTabIconId(): Int {
        return R.drawable.ic_local_dining_black_24dp
    }

    override fun getTabTitle(): String {
        return "FOOD"
    }

    companion object {
        const val REQUEST_CODE_RESTAURANT_STREET = 0
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, LocationBroadCastReceiver::class.java)

        intent.action = LocationBroadCastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_RESTAURANT_STREET,
            intent,
            FLAG_UPDATE_CURRENT
        )
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

}
