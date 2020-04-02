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

    private val mutableLoadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = mutableLoadingState

    fun walkRestaurantStreet(location: Location, index: Index = Index(1)) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableLoadingState.postValue(true)
            update(repository.fetchRestaurantStreet(location, Range(5), index))
            mutableLoadingState.postValue(false)
        }
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

    private fun update(restaurantStreet: RestaurantStreet, ifEmpty: () -> Unit = {}) {
        when (restaurantStreet.restaurantDataList.isNotEmpty()) {
            false -> ifEmpty()
            true -> mutableRestaurantStreet.postValue(
                (mutableRestaurantStreet.value ?: EmptyRestaurantStreet) + restaurantStreet
            )
        }
    }
}
