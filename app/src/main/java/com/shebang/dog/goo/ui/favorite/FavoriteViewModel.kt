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

    private val mutableLoadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = mutableLoadingState

    fun walkFavoriteRestaurantStreet() = viewModelScope.launch(Dispatchers.IO) {
        mutableLoadingState.postValue(true)
        val favoriteRestaurantStreet =
            repository.fetchRestaurantStreet().restaurantDataList.filter { it.favorite.value }

        mutableRestaurantStreet.postValue(RestaurantStreet(favoriteRestaurantStreet))
        mutableLoadingState.postValue(false)
    }

}