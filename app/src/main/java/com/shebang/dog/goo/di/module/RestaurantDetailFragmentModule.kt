package com.shebang.dog.goo.di.module

import com.shebang.dog.goo.ui.main.detail.RestaurantDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RestaurantDetailFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeRestaurantDetailFragment(): RestaurantDetailFragment
}