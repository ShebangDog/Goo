package com.shebang.dog.goo.di

import com.shebang.dog.goo.ui.street.RestaurantStreetFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RestaurantStreetFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeRestaurantStreetFragment(): RestaurantStreetFragment
}