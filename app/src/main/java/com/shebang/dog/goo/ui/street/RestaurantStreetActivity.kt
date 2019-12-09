package com.shebang.dog.goo.ui.street

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.databinding.ActivityRestaurantListBinding
import com.shebang.dog.goo.di.RestaurantRepositoryInjection
import com.shebang.dog.goo.factory.ViewModelFactory

class RestaurantStreetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantStreetViewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(RestaurantRepositoryInjection.inject(application))
            )
                .get(RestaurantStreetViewModel::class.java)

        val restaurantStreetAdapter = RestaurantStreetAdapter()

        restaurantStreetViewModel.restaurantStreet
            .observe(this) {
                restaurantStreetAdapter.restaurantStreet = it
            }

        val recyclerView = findViewById<RecyclerView>(R.id.restaurant_list_recycler_view)

        recyclerView.apply {
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
