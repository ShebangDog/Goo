package com.shebang.dog.goo.di

import com.shebang.dog.goo.BuildConfig
import com.shebang.dog.goo.data.FakeLocalDataSource
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApi
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApiClient
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApiClientImpl
import com.shebang.dog.goo.di.scope.LocalDataSource
import com.shebang.dog.goo.di.scope.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class TestApplicationModule {

    @Singleton
    @LocalDataSource
    @Binds
    abstract fun provideLocalDataSource(fake: FakeLocalDataSource): RestaurantDataSource

    @Module
    companion object {
        @JvmStatic
        @Singleton
        @RemoteDataSource
        @Provides
        fun provideRemoteDataSource(apiClient: HotpepperApiClient): RestaurantDataSource {
            return RestaurantRemoteDataSource(apiClient)
        }

        @JvmStatic
        @Singleton
        @Provides
        fun provideHotpepperApiClient(): HotpepperApiClient {
            fun createRetrofitBuilder(): Retrofit {
                return Retrofit.Builder()
                    .baseUrl("https://webservice.recruit.co.jp/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return HotpepperApiClientImpl(
                createRetrofitBuilder().create(HotpepperApi::class.java),
                BuildConfig.HOTPEPPER_API_KEY
            )
        }
    }
}