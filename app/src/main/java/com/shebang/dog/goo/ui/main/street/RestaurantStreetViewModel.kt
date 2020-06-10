package com.shebang.dog.goo.ui.main.street

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.query.Index
import com.shebang.dog.goo.data.model.query.Range
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantStreetViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    private val mutableRestaurantStreet = MutableLiveData<List<RestaurantData>>()
    val restaurantStreet: LiveData<List<RestaurantData>>
        get() = mutableRestaurantStreet

    val favoriteList = liveData {
        repository.fetchRestaurantStreet()
            .map { restaurantDataList -> restaurantDataList.filter { it.favorite.value } }
            .collect { emit(it) }
    }

    private val mutableLoadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = mutableLoadingState

    fun isEmptyRestaurantStreet(): Boolean {
        return restaurantStreet.value.isNullOrEmpty()
    }

    @ExperimentalCoroutinesApi
    fun walkRestaurantStreet(location: Location, index: Index = Index(1)) = viewModelScope.launch {
        repository.fetchRestaurantStreet(
            location,
            Range(5), index
        )
            .onStart { mutableLoadingState.value = true }
            .collect {
                val concatStreet = (mutableRestaurantStreet.value ?: emptyList()) + it

                mutableRestaurantStreet.value = concatStreet
                mutableLoadingState.value = false
            }
    }

    fun toggleFavorite(
        restaurantData: RestaurantData
    ) = viewModelScope.launch {
        restaurantData.switchFavorite()
        repository.saveRestaurant(restaurantData)
    }

}
