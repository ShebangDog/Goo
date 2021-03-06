package com.shebang.dog.goo.di.module

import android.content.Context
import androidx.room.Room
import com.shebang.dog.goo.BuildConfig
import com.shebang.dog.goo.data.RestaurantDataSource
import com.shebang.dog.goo.data.local.RestaurantDao
import com.shebang.dog.goo.data.local.RestaurantDatabase
import com.shebang.dog.goo.data.local.RestaurantLocalDataSource
import com.shebang.dog.goo.data.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.data.remote.api.gurumenavi.GurumenaviApi
import com.shebang.dog.goo.data.remote.api.gurumenavi.GurumenaviApiClient
import com.shebang.dog.goo.data.remote.api.gurumenavi.GurumenaviApiClientImpl
import com.shebang.dog.goo.data.remote.api.hotpepper.HotpepperApi
import com.shebang.dog.goo.data.remote.api.hotpepper.HotpepperApiClient
import com.shebang.dog.goo.data.remote.api.hotpepper.HotpepperApiClientImpl
import com.shebang.dog.goo.di.annotation.scope.LocalDataSource
import com.shebang.dog.goo.di.annotation.scope.RemoteDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideRemoteDataSource(
        hotpepperApiClient: HotpepperApiClient,
        gurumenaviApiClient: GurumenaviApiClient
    ): RestaurantDataSource {

        return RestaurantRemoteDataSource(hotpepperApiClient, gurumenaviApiClient)
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
        return HotpepperApiClientImpl(
            createRetrofitBuilder("https://webservice.recruit.co.jp/").create(HotpepperApi::class.java),
            BuildConfig.HOTPEPPER_API_KEY
        )
    }

    @Singleton
    @Provides
    fun provideGurumenaviApiClient(): GurumenaviApiClient {
        return GurumenaviApiClientImpl(
            createRetrofitBuilder("https://api.gnavi.co.jp/").create(GurumenaviApi::class.java),
            BuildConfig.GURUMENAVI_API_KEY
        )
    }

    private fun createRetrofitBuilder(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}