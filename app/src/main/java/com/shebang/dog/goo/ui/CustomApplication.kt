package com.shebang.dog.goo.ui

import android.app.Application
import com.shebang.dog.goo.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class CustomApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        DaggerApplicationComponent.factory().create(this).inject(this)
        return dispatchingAndroidInjector
    }
}