package com.shebang.dog.goo.di

import android.content.Context
import com.shebang.dog.goo.ui.CustomApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AndroidInjectionModule::class, RestaurantStreetFragmentModule::class])
interface ApplicationComponent : AndroidInjector<CustomApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}