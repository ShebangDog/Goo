package com.shebang.dog.goo.ui

import android.app.Application
import com.shebang.dog.goo.di.ApplicationComponent
import com.shebang.dog.goo.di.DaggerApplicationComponent

open class CustomApplication : Application() {
    val applicationComponent by lazy { applicationComponentInitializer() }

    open fun applicationComponentInitializer(): ApplicationComponent {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }
}