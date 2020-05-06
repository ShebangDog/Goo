package com.shebang.dog.goo.di.module

import com.shebang.dog.goo.ui.main.about.AboutActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AboutActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeAboutItemActivity(): AboutActivity
}