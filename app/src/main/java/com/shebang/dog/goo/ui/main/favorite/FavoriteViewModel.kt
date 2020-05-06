package com.shebang.dog.goo.ui.main.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.data.model.RestaurantStreet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
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

    @ExperimentalCoroutinesApi
    fun walkFavoriteRestaurantStreet() = viewModelScope.launch {
        repository.fetchRestaurantStreet()
            .onStart { mutableLoadingState.value = true }
            .collect { street ->
                val favoriteStreet = RestaurantStreet(
                    street.restaurantDataList.filter { it.favorite.value }
                )

                mutableRestaurantStreet.value = favoriteStreet
                mutableLoadingState.value = false
            }
    }

}