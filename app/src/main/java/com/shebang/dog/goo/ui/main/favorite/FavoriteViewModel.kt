package com.shebang.dog.goo.ui.main.favorite

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    val favoriteList: LiveData<List<RestaurantData>> = liveData {
        repository.fetchRestaurantStreet().collect { restaurantList ->
            val filtered = restaurantList.filter { it.favorite.value }
            emit(filtered)
        }
    }

    private val mutableLoadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = mutableLoadingState

    @ExperimentalCoroutinesApi
    fun walkFavoriteRestaurantStreet() = viewModelScope.launch {
        repository.fetchRestaurantStreet()
            .onStart { mutableLoadingState.value = true }
            .collect { street ->
                mutableLoadingState.value = false
            }
    }

}