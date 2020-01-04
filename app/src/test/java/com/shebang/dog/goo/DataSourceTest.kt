package com.shebang.dog.goo

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.shebang.dog.goo.data.model.FindData
import com.shebang.dog.goo.data.model.Range
import com.shebang.dog.goo.data.repository.RestaurantRepository
import com.shebang.dog.goo.data.repository.remote.RestaurantRemoteDataSource
import com.shebang.dog.goo.di.ApplicationModule
import com.shebang.dog.goo.fake.FakeLocalDataSource
import com.shebang.dog.goo.fake.FakeReturnValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object DataSourceTest : Spek({
    val localDataSource by memoized {
        mock<FakeLocalDataSource> {
            onBlocking {
                fetchRestaurantStreet(any(), any())
            } doReturn FindData.NotFound()
        }
    }
    val remoteDataSource by memoized {
        mock<RestaurantRemoteDataSource> {
            onBlocking {
                fetchRestaurantStreet(any(), any())
            } doReturn FindData.NotFound()
        }
    }

    val applicationModule = ApplicationModule()
    val repository by memoized {
        RestaurantRepository(
            localDataSource,
            applicationModule.provideRemoteDataSource(
                applicationModule.provideHotpepperApiClient()
            )
        )
    }

    val location = FakeReturnValue.location

    val range = Range(1)

    Feature("Repositoryのテスト") {
        Scenario("Repositoryのテスト") {
            Given("位置と範囲の設定") {

            }

            Then("NotFoundを返す") {
                runBlocking {

                    val result = when (val fetchRestaurantStreet =
                        repository.fetchRestaurantStreet(location, range)) {

                        is FindData.NotFound -> "notfound"
                        is FindData.Found -> fetchRestaurantStreet.value.restaurantDataList
                            .joinToString("\n") { it.name }
                    }

                    assertNotEquals(result, "notfound")
                }
            }
        }

        Scenario("Repositoryのテスト") {
            Given("位置情報と範囲の設定") {

            }

            Then("NotFoundを返す") {
                runBlocking {
                    val result = when (val fetchRestaurantStreet =
                        repository.fetchRestaurantStreet(location, range)) {

                        is FindData.NotFound -> "notfound"
                        is FindData.Found -> fetchRestaurantStreet.value.restaurantDataList
                            .joinToString("\n") { it.name }
                    }

                    assertNotEquals("notfound", result)
                }
            }
        }

        Scenario("Repositoryのテスト") {
            Given("ニセの値を発見させる") {

            }

            Then("restaurantStreetForTestingと同じ値を返す") {
                runBlocking {
                    val result = when (val fetchRestaurantStreet =
                        repository.fetchRestaurantStreet(location, range)) {

                        is FindData.NotFound -> "notfound"
                        is FindData.Found -> {
                            val string = fetchRestaurantStreet.value.restaurantDataList
                                .joinToString("\n") { it.name }

                            println(string)

                            string
                        }
                    }

                    assertNotEquals(
                        "notfound",
                        result
                    )
                }
            }

        }
    }
})