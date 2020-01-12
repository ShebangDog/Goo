package com.shebang.dog.goo.di

import android.content.Context
import com.shebang.dog.goo.ui.street.RestaurantStreetActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: RestaurantStreetActivity)
}