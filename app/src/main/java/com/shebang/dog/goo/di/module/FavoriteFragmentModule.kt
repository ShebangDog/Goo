package com.shebang.dog.goo.di.module

import com.shebang.dog.goo.ui.main.favorite.FavoriteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment
}