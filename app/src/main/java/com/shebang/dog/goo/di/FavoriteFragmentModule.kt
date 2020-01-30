package com.shebang.dog.goo.di

import com.shebang.dog.goo.ui.favorite.FavoriteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment
}