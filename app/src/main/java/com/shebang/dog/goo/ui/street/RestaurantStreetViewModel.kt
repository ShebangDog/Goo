package com.shebang.dog.goo.ui.street

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantStreetViewModel(private val repository: RestaurantRepository) : ViewModel() {
    private val mutableRestaurantStreet = MutableLiveData<RestaurantStreet>()
    val restaurantStreet: LiveData<RestaurantStreet>
        get() = mutableRestaurantStreet

    init {
        viewModelScope.launch {
            update().join()
        }
    }

    fun update() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = repository.fetchRestaurantStreet(
            Latitude(35.669220),
            Longitude(139.761457),
            Range(1)
        )) {
            is FindData.Found -> mutableRestaurantStreet.postValue(result.value)
        }
    }

    fun save(restaurantData: RestaurantData) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveRestaurant(restaurantData)
        update()
    }

    fun delete(id: Id) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRestaurantData(id)
        update()
    }
}