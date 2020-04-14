package com.shebang.dog.goo.di.module

import androidx.lifecycle.ViewModel
import com.shebang.dog.goo.di.annotation.key.ViewModelKey
import com.shebang.dog.goo.ui.detail.RestaurantDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RestaurantDetailViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RestaurantDetailViewModel::class)
    abstract fun bindRestaurantDetailViewModel(viewModel: RestaurantDetailViewModel): ViewModel
}