package com.shebang.dog.goo.ui.street

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.shebang.dog.goo.model.FindData
import com.shebang.dog.goo.model.RestaurantStreet
import com.shebang.dog.goo.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers

class RestaurantStreetViewModel(private val repository: RestaurantRepository) : ViewModel() {
    val restaurantStreet: LiveData<RestaurantStreet> = liveData(Dispatchers.IO) {
        val data = when (val result = repository.fetchRestaurantStreet()) {
            is FindData.NotFound -> RestaurantStreet(emptyList())
            is FindData.Found -> result.value
        }

        emit(data)
    }
}