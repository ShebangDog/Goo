package com.shebang.dog.goo.di.module

import androidx.lifecycle.ViewModel
import com.shebang.dog.goo.di.annotation.key.ViewModelKey
import com.shebang.dog.goo.ui.favorite.FavoriteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavoriteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}