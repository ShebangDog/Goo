package com.shebang.dog.goo.di

import com.shebang.dog.goo.ui.street.RestaurantStreetActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RestaurantStreetActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeAndroidInjector(): RestaurantStreetActivity
}