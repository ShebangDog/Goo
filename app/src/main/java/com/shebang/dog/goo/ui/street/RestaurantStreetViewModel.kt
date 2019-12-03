package com.shebang.dog.goo.ui.street

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers

class RestaurantStreetViewModel(private val repository: RestaurantRepository) : ViewModel() {
    val restaurantStreet: LiveData<RestaurantStreet> = liveData(Dispatchers.IO) {
        val data = repository.getRestaurants()
        emit(data)
    }
}