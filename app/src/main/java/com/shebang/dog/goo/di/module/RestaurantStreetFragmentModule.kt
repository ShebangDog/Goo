package com.shebang.dog.goo.di.module

import com.shebang.dog.goo.ui.main.street.RestaurantStreetFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RestaurantStreetFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeRestaurantStreetFragment(): RestaurantStreetFragment
}