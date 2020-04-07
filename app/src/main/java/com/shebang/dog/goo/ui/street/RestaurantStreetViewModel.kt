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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantStreetViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {
    private val mutableRestaurantStreet = MutableLiveData<RestaurantStreet>()
    val restaurantStreet: LiveData<RestaurantStreet>
        get() = mutableRestaurantStreet

    private val mutableLoadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = mutableLoadingState

    @ExperimentalCoroutinesApi
    fun walkRestaurantStreet(location: Location, index: Index = Index(1)) {
        viewModelScope.launch {
            repository.fetchRestaurantStreet(location, Range(5), index)
                .onStart { mutableLoadingState.value = true }
                .collect {
                    val concatStreet = (mutableRestaurantStreet.value ?: EmptyRestaurantStreet) + it

                    mutableRestaurantStreet.value = concatStreet
                    mutableLoadingState.value = false
                }
        }
    }

    fun toggleFavorite(
        restaurantData: RestaurantData,
        imageButton: ImageButton,
        favorite: Drawable?,
        border: Drawable?
    ) = viewModelScope.launch {
        imageButton.setImageDrawable(if (imageButton.isSelected) favorite else border)
        restaurantData.switchFavorite()

        repository.saveRestaurant(restaurantData)
    }

}
