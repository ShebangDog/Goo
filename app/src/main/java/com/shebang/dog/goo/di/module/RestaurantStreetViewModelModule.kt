package com.shebang.dog.goo.di.module

import androidx.lifecycle.ViewModel
import com.shebang.dog.goo.di.annotation.key.ViewModelKey
import com.shebang.dog.goo.ui.street.RestaurantStreetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RestaurantStreetViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RestaurantStreetViewModel::class)
    abstract fun bindRestaurantStreetViewModel(viewModel: RestaurantStreetViewModel): ViewModel
}