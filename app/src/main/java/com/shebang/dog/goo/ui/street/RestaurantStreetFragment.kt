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
import com.shebang.dog.goo.service.LocationBroadCastReceiver
import com.shebang.dog.goo.ui.tab.TabbedFragment
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor.KEY_LOCATION_RESULT
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RestaurantStreetFragment : TabbedFragment(R.layout.fragment_restaurant_list),
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var restaurantStreetViewModel: RestaurantStreetViewModel

    @Inject
    lateinit var restaurantStreetAdapter: RestaurantStreetAdapter

    private lateinit var binding: FragmentRestaurantListBinding
    private lateinit var safeContext: Context

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            safeContext
        )
    }

    private var currentLocation: Location? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        safeContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantListBinding.bind(view)

        if (!checkPermissions()) requestPermissions()

        restaurantStreetViewModel.restaurantStreet
            .observe(this) { restaurantStreetAdapter.restaurantStreet = it }

        binding.restaurantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(safeContext)
            adapter = restaurantStreetAdapter
        }

        LocationSharedPreferenceAccessor.registerOnSharedPreferenceChangeListener(safeContext) {
            if (it == KEY_LOCATION_RESULT) {
                currentLocation = LocationSharedPreferenceAccessor.getLocationResult(safeContext)
                restaurantStreetViewModel.update(currentLocation!!)
            }
        }

        fusedLocationClient.apply {
            val locationRequest = LocationRequest.create().apply {
                fastestInterval = 5000
                interval = 1800000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            requestLocationUpdates(locationRequest, createPendingIntent())

            lastLocation.addOnSuccessListener {
                val location = Location(
                    Latitude(it.latitude),
                    Longitude(it.longitude)
                )

                LocationSharedPreferenceAccessor.setLocationResult(
                    safeContext,
                    location
                )
            }
        }

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
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

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(safeContext, LocationBroadCastReceiver::class.java)

        intent.action = LocationBroadCastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(
            safeContext,
            REQUEST_CODE_RESTAURANT_STREET,
            intent,
            FLAG_UPDATE_CURRENT
        )
    }

    private fun checkPermissions(): Boolean {
        val fineLocationPermissionState = ActivityCompat.checkSelfPermission(
            safeContext, Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocationPermissionState = ActivityCompat.checkSelfPermission(
            safeContext, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val isGranted = PackageManager.PERMISSION_GRANTED

        return fineLocationPermissionState == isGranted && coarseLocationPermissionState == isGranted
    }

    private fun requestPermissions() {
        val isGranted = PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat.checkSelfPermission(
            safeContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == isGranted

        val permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(
            safeContext, Manifest.permission.ACCESS_COARSE_LOCATION
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
