package com.shebang.dog.goo.ui.detail

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.model.location.Location
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.model.restaurant.RestaurantData
import com.shebang.dog.goo.util.GoogleMapUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantDetailViewModel @Inject constructor(repository: RestaurantRepository) :
    ViewModel() {

    private val restaurantId: MutableLiveData<Id> = MutableLiveData()
    val restaurantData = restaurantId.switchMap {
        liveData<RestaurantData?> {
            emit(repository.fetchRestaurant(it))
        }
    }

    val restaurantImageUrlList = restaurantData.switchMap {
        liveData<List<String>> {
            emit(it?.imageUrl?.stringList ?: emptyList())
        }
    }

    fun showDetail(id: Id) = viewModelScope.launch {
        restaurantId.value = id
    }

    fun navigateToRestaurant(currentLocation: Location?, uriReceiver: (String) -> Unit) {
        val userLocation = currentLocation ?: return
        val restaurantLocation = restaurantData.value?.location ?: return

        val uri = GoogleMapUtil.createUri(userLocation, restaurantLocation)

        uriReceiver(uri)
    }
}