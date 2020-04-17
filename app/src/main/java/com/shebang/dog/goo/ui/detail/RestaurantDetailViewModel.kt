package com.shebang.dog.goo.ui.detail

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.model.restaurant.Id
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantDetailViewModel @Inject constructor(repository: RestaurantRepository) :
    ViewModel() {

    private val restaurantId: MutableLiveData<Id> = MutableLiveData()
    val restaurantData = restaurantId.switchMap {
        liveData {
            emit(repository.fetchRestaurant(it))
        }
    }

    val restaurantImageUrlList = restaurantData.map { it?.imageUrl?.stringList ?: emptyList() }

    fun showDetail(id: Id) = viewModelScope.launch {
        restaurantId.value = id
    }
}