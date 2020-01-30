package com.shebang.dog.goo.ui.street

import android.graphics.drawable.Drawable
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shebang.dog.goo.data.model.*
import com.shebang.dog.goo.data.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantStreetViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {
    private val mutableRestaurantStreet = MutableLiveData<RestaurantStreet>()
    val restaurantStreet: LiveData<RestaurantStreet>
        get() = mutableRestaurantStreet

    fun update(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        update(
            repository.fetchRestaurantStreet(
                location,
                Range(1)
            )
        )
    }

    fun toggleFavorite(
        restaurantData: RestaurantData,
        imageButton: ImageButton,
        favorite: Drawable?,
        border: Drawable?
    ) = viewModelScope.launch(Dispatchers.IO) {
        imageButton.setImageDrawable(if (imageButton.isSelected) favorite else border)
        restaurantData.switchFavorite()

        repository.saveRestaurant(restaurantData)
    }

    fun save(restaurantData: RestaurantData) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveRestaurant(restaurantData)
    }

    fun delete(id: Id) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRestaurantData(id)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRestaurants()
    }

    private fun update(restaurantStreet: RestaurantStreet, ifEmpty: () -> Unit = {}) {
        when (restaurantStreet.restaurantDataList.isNotEmpty()) {
            true -> mutableRestaurantStreet.postValue(restaurantStreet)
            false -> ifEmpty.invoke()
        }
    }
}
