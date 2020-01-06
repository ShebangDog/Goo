package com.shebang.dog.goo.di

import android.content.Context
import androidx.room.Room
import com.shebang.dog.goo.BuildConfig
import com.shebang.dog.goo.data.repository.RestaurantDataSource
import com.shebang.dog.goo.data.repository.local.RestaurantDao
import com.shebang.dog.goo.data.repository.local.RestaurantDatabase
import com.shebang.dog.goo.data.repository.local.RestaurantLocalDataSource
import com.shebang.dog.goo.data.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.data.repository.remote.api.hotpepper.HotpepperApi
import com.shebang.dog.goo.data.repository.remote.api.hotpepper.HotpepperApiClient
import com.shebang.dog.goo.data.repository.remote.api.hotpepper.HotpepperApiClientImpl
import com.shebang.dog.goo.di.scope.LocalDataSource
import com.shebang.dog.goo.di.scope.RemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule {
    private val databaseName: String = "restaurant_database"

    @Singleton
    @LocalDataSource
    @Provides
    fun provideLocalDataSource(restaurantDao: RestaurantDao): RestaurantDataSource {
        return RestaurantLocalDataSource(restaurantDao)
    }

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(apiClient: HotpepperApiClient): RestaurantDataSource {
        return RestaurantRemoteDataSource(apiClient)
    }

    @Singleton
    @Provides
    fun provideDao(context: Context): RestaurantDao {
        return Room.databaseBuilder(
            context,
            RestaurantDatabase::class.java,
            databaseName
        ).build().restaurantDao()
    }

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