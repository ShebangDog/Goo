package com.shebang.dog.goo.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.shebang.dog.goo.data.repository.RestaurantRepository
import com.shebang.dog.goo.factory.ViewModelFactory
import com.shebang.dog.goo.ui.street.RestaurantStreetViewModel

object RestaurantStreetViewModelInjection {
    fun inject(
        activity: FragmentActivity,
        repository: RestaurantRepository
    ): RestaurantStreetViewModel {

        return ViewModelProviders.of(activity, ViewModelFactory(repository))
            .get(RestaurantStreetViewModel::class.java)
    }
}