package com.shebang.dog.goo.ui.street

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.shebang.dog.goo.databinding.ActivityRestaurantListBinding
import com.shebang.dog.goo.service.LocationBroadCastReceiver
import com.shebang.dog.goo.ui.CustomApplication
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor.KEY_LOCATION_RESULT
import javax.inject.Inject

class RestaurantStreetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    private lateinit var restaurantStreetAdapter: RestaurantStreetAdapter

    @Inject
    lateinit var restaurantStreetViewModel: RestaurantStreetViewModel

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as CustomApplication).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkPermissions()) requestPermissions()

        restaurantStreetAdapter =
            RestaurantStreetAdapter { restaurantData, imageButton, favorite, border ->
                imageButton.isSelected = !imageButton.isSelected

                restaurantStreetViewModel.clickFavorite(
                    restaurantData,
                    imageButton,
                    favorite,
                    border
                )
            }

        restaurantStreetViewModel.restaurantStreet
            .observe(this) { restaurantStreetAdapter.restaurantStreet = it }

        binding.restaurantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RestaurantStreetActivity)
            adapter = restaurantStreetAdapter
        }

        LocationSharedPreferenceAccessor.registerOnSharedPreferenceChangeListener(this) {
            if (it == KEY_LOCATION_RESULT) {
                currentLocation = LocationSharedPreferenceAccessor.getLocationResult(this)
                restaurantStreetViewModel.update(currentLocation!!)
            }
        }

        fusedLocationClient.apply {
            val restaurantStreetActivity = this@RestaurantStreetActivity
            val locationRequest = LocationRequest.create().apply {
                fastestInterval = 5000
                interval = 1800000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            requestLocationUpdates(locationRequest, createPendingIntent())

            lastLocation.addOnSuccessListener {
                if (it != null) {
                    val location = Location(
                        Latitude(it.latitude),
                        Longitude(it.longitude)
                    )

                    LocationSharedPreferenceAccessor.setLocationResult(
                        restaurantStreetActivity,
                        location
                    )
                }
            }
        }

    }

    companion object {
        const val REQUEST_CODE_RESTAURANT_STREET = 0
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, LocationBroadCastReceiver::class.java)

        intent.action = LocationBroadCastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_RESTAURANT_STREET,
            intent,
            FLAG_UPDATE_CURRENT
        )
    }

    private fun checkPermissions(): Boolean {
        val fineLocationPermissionState = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocationPermissionState = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val isGranted = PackageManager.PERMISSION_GRANTED

        return fineLocationPermissionState == isGranted && coarseLocationPermissionState == isGranted
    }

    private fun requestPermissions() {
        val isGranted = PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == isGranted

        val permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == isGranted

        val shouldProvideRationale =
            permissionAccessFineLocationApproved && permissionAccessCoarseLocationApproved

        if (shouldProvideRationale) {
            Snackbar.make(
                findViewById<View>(R.id.restaurant_list_activity),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        34
                    )
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                34
            )
        }

    }
}
