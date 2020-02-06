package com.shebang.dog.goo.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.data.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    private val mutableRestaurantStreet = MutableLiveData<RestaurantStreet>()
    val restaurantStreet: LiveData<RestaurantStreet>
        get() = mutableRestaurantStreet

    fun walkFavoriteRestaurantStreet() = viewModelScope.launch(Dispatchers.IO) {
        val favoriteRestaurantStreet =
            repository.fetchRestaurantStreet().restaurantDataList.filter { it.favorite.value }

        favoriteRestaurantStreet.also {
            if (it.isNotEmpty()) mutableRestaurantStreet.postValue(RestaurantStreet(it))
        }
    }

}