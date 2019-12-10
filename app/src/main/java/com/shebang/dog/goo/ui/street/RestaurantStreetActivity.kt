package com.shebang.dog.goo.ui.street

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.databinding.ActivityRestaurantListBinding
import com.shebang.dog.goo.di.RestaurantRepositoryInjection
import com.shebang.dog.goo.di.RestaurantStreetViewModelInjection

class RestaurantStreetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    private val restaurantStreetAdapter by lazy { RestaurantStreetAdapter() }
    private val restaurantStreetViewModel by lazy {
        RestaurantStreetViewModelInjection.inject(
            this,
            RestaurantRepositoryInjection.inject(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
