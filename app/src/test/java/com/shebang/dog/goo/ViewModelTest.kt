package com.shebang.dog.goo

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.shebang.dog.goo.data.model.FindData
import com.shebang.dog.goo.data.model.RestaurantStreet
import com.shebang.dog.goo.data.repository.RestaurantRepository
import com.shebang.dog.goo.extension.useLiveData
import com.shebang.dog.goo.fake.FakeReturnValue
import com.shebang.dog.goo.ui.street.RestaurantStreetViewModel
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ViewModelTest : Spek({
    useLiveData()

    Feature("ViewModel") {
        val repository = mock<RestaurantRepository>()
        val viewModel = RestaurantStreetViewModel(repository)
        val observer = mock<Observer<RestaurantStreet>>()

        Scenario("位置情報を受け取った際の挙動を確認") {
            Given("") {
                runBlocking {
                    whenever(
                        repository.fetchRestaurantStreet(
                            any(),
                            any()
                        )
                    ).thenReturn(FindData.Found(FakeReturnValue.restaurantStreetForTesting))
                }

                viewModel.restaurantStreet.observeForever(observer)
            }

            When("現在のliveDataの値を保存する") {
                viewModel.update(FakeReturnValue.location)
            }

            Then("Fakeの値が確認できる") {
                verify(observer).onChanged(FakeReturnValue.restaurantStreetForTesting)
            }
        }
    }
})