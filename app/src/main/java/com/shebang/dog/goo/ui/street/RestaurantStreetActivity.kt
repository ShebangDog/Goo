package com.shebang.dog.goo.ui.street

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.ActivityRestaurantListBinding
import com.shebang.dog.goo.factory.ViewModelFactory
import com.shebang.dog.goo.model.*
import com.shebang.dog.goo.repository.RestaurantRepository
import com.shebang.dog.goo.repository.local.RestaurantDatabase
import com.shebang.dog.goo.repository.local.RestaurantLocalDataSource
import com.shebang.dog.goo.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.repository.remote.api.HotpepperApi
import com.shebang.dog.goo.repository.remote.api.HotpepperApiClientImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantStreetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = RestaurantDatabase.getDataBase(application)!!
        val localDataSource = RestaurantLocalDataSource(database)
        val remoteDataSource = RestaurantRemoteDataSource(
            HotpepperApiClientImpl(
                Retrofit.Builder()
                    .baseUrl("https://webservice.recruit.co.jp/")
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build()
                    .create(
                        HotpepperApi::class.java
                    ),
                "1071f9f5bdb7f5b0"
            )
        )

        val repository = RestaurantRepository(localDataSource, remoteDataSource)
        val restaurantStreetViewModel =
            ViewModelProviders.of(this, ViewModelFactory(repository))
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

        runBlocking(Dispatchers.IO) {
            restaurantStreetViewModel.update().join()
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

}
