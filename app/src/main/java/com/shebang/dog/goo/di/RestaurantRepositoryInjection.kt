package com.shebang.dog.goo.di

import android.content.Context
import com.shebang.dog.goo.BuildConfig
import com.shebang.dog.goo.data.repository.RestaurantRepository
import com.shebang.dog.goo.data.repository.local.RestaurantDatabase
import com.shebang.dog.goo.data.repository.local.RestaurantLocalDataSource
import com.shebang.dog.goo.data.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApi
import com.shebang.dog.goo.data.repository.remote.api.HotpepperApiClientImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestaurantRepositoryInjection {
    fun inject(context: Context): RestaurantRepository {
        return RestaurantRepository(injectLocalDataSource(context), injectRemoteDataSource())
    }

    private fun injectLocalDataSource(context: Context): RestaurantLocalDataSource {
        fun createRestaurantDatabase(context: Context): RestaurantDatabase {
            return RestaurantDatabase.getDataBase(context)!!
        }

        val database = createRestaurantDatabase(context)

        return RestaurantLocalDataSource(database)
    }

    private fun injectRemoteDataSource(): RestaurantRemoteDataSource {
        fun createRetrofitBuilder(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://webservice.recruit.co.jp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiClient = HotpepperApiClientImpl(
            createRetrofitBuilder().create(HotpepperApi::class.java),
            BuildConfig.HOTPEPPER_API_KEY
        )

        return RestaurantRemoteDataSource(apiClient)
    }
}