package com.mbahgojol.storyapp.ui.home.maplist

import androidx.lifecycle.Observer
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.helper.InstantRuleExecution
import com.mbahgojol.storyapp.helper.TrampolineSchedulerRX
import com.mbahgojol.storyapp.utils.ResultState
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Single
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class MapListViewModelTest : Spek({
    Feature("Map List Story") {
        val repository = mock<Repository>()
        val viewModel = MapListViewModel(repository)
        val observerState = mock<Observer<ResultState>>()

        beforeFeature {
            TrampolineSchedulerRX.start()
            InstantRuleExecution.start()
            viewModel.storyListState.observeForever(observerState)
        }

        Scenario("do on Map List Story, response success") {
            val expectedResult = GetAllStoryResponse(false, mutableListOf(), "")

            Given("set success result state") {
                given(repository.getAllStory(hashMapOf())).willReturn(
                    Single.just(
                        expectedResult
                    )
                )
            }

            When("map list story") {
                viewModel.getListStory()
            }

            Then("result success and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Success(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        Scenario("do on Map List Story, response error") {
            val expectedResult = Throwable("error")

            Given("set error result state") {
                given(repository.getAllStory(hashMapOf())).willReturn(
                    Single.error(expectedResult)
                )
            }

            When("map list story") {
                viewModel.getListStory()
            }

            Then("result error and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Error(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        afterFeature {
            viewModel.storyListState.removeObserver(observerState)
            TrampolineSchedulerRX.tearDown()
            InstantRuleExecution.tearDown()
        }
    }
})