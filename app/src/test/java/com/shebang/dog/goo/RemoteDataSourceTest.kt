package com.shebang.dog.goo

import com.shebang.dog.goo.data.model.FindData
import com.shebang.dog.goo.data.model.Range
import com.shebang.dog.goo.data.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.di.ApplicationModule
import com.shebang.dog.goo.fake.FakeReturnValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object RemoteDataSourceTest : Spek({
    val remoteDataSource =
        RestaurantRemoteDataSource(ApplicationModule().provideHotpepperApiClient())

    val location = FakeReturnValue.location
    val range = Range(1)

    Feature("RemoteDataSourceのテスト") {
        Scenario("店舗の情報を受け取れているかのテスト") {
            Then("Foundを返す") {
                runBlocking {
                    val message = when (val result =
                        remoteDataSource.fetchRestaurantStreet(location, range)) {

                        is FindData.NotFound -> "notfound"
                        is FindData.Found -> "found"
                    }

                    assertEquals(message, "found")
                }
            }
        }
    }
})