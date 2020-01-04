package com.shebang.dog.goo.di

import com.shebang.dog.goo.ui.CustomApplication

class TestCustomApplication : CustomApplication() {
    override fun applicationComponentInitializer(): ApplicationComponent {
        return DaggerTestApplicationComponent.create()
    }
}