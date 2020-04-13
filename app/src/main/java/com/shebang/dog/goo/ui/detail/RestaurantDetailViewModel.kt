package com.shebang.dog.goo.ui.detail

import androidx.lifecycle.*
import com.shebang.dog.goo.data.RestaurantRepository
import com.shebang.dog.goo.model.Id
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

    fun showDetail(id: Id) = viewModelScope.launch {
        restaurantId.value = id
    }
}