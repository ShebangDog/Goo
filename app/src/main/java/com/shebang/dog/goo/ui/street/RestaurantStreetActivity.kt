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
import com.shebang.dog.goo.util.DebugHelper
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor.KEY_LOCATION_RESULT
import javax.inject.Inject

class RestaurantStreetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    private val restaurantStreetAdapter by lazy { RestaurantStreetAdapter() }

    @Inject
    lateinit var restaurantStreetViewModel: RestaurantStreetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as CustomApplication).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restaurantStreetViewModel.restaurantStreet
            .observe(this) { restaurantStreetAdapter.restaurantStreet = it }

        binding.restaurantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RestaurantStreetActivity)
            adapter = restaurantStreetAdapter
        }

        restaurantStreetViewModel.update()
        restaurantStreetViewModel.save(
            RestaurantData(
                Id("da"),
                "nammmm",
                "",
                Location(Latitude(1.0), Longitude(1.0))
            )
        )
    }

}
