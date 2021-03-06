package com.shebang.dog.goo.ui.main.detail

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.restaurant.Id
import com.shebang.dog.goo.data.model.restaurant.PhoneNumber
import com.shebang.dog.goo.data.model.restaurant.RestaurantData
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
        val restaurantName = restaurantData.value?.name?.value ?: return

        val uri = GoogleMapUtil.createUri(
            userLocation,
            restaurantName
        )

        uriReceiver(uri)
    }

    fun callingRestaurant(callingFunction: (PhoneNumber) -> Unit) {
        val phoneNumber = restaurantData.value?.phoneNumber ?: return

        callingFunction(phoneNumber)
    }
}