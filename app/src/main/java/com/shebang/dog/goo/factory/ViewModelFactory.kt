package com.shebang.dog.goo.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shebang.dog.goo.repository.RestaurantRepository
import com.shebang.dog.goo.ui.street.RestaurantStreetViewModel

class ViewModelFactory(private val repository: RestaurantRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestaurantStreetViewModel(repository) as T
    }
}